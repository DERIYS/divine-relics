package com.deriys.divinerelics.structures;

import com.deriys.divinerelics.config.DivineRelicsCommonConfig;
import com.deriys.divinerelics.init.DRStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class NoWaterNearbyStructure extends Structure {

    public static final Codec<NoWaterNearbyStructure> CODEC = RecordCodecBuilder.<NoWaterNearbyStructure>mapCodec(instance ->
            instance.group(NoWaterNearbyStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 7).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, NoWaterNearbyStructure::new)).codec();

    public static final List<String> WATER_BIOMES = Arrays.asList(
            "minecraft:river",
            "minecraft:frozen_river",
            "minecraft:beach",
            "minecraft:snowy_beach",
            "minecraft:stony_shore",
            "minecraft:swamp",
            "minecraft:ocean",
            "minecraft:cold_ocean",
            "minecraft:deep_cold_ocean",
            "minecraft:deep_frozen_ocean",
            "minecraft:frozen_ocean",
            "minecraft:warm_ocean",
            "minecraft:lukewarm_ocean",
            "minecraft:deep_lukewarm_ocean",
            "minecraft:deep_ocean"
    );

    private static final Set<ResourceKey<Biome>> WATER_BIOME_KEYS = WATER_BIOMES.stream()
            .map(id -> ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(id)))
            .collect(Collectors.toUnmodifiableSet());

    /** Starts whose terrain height (at the chunk corner) reaches this value are rejected. */
    private static final int MAX_START_HEIGHT = 90;
    /** Half-size of the area around the start piece that must not be covered by open water (the hut is 9x15). */
    private static final int SURFACE_CHECK_RADIUS = 7;

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public NoWaterNearbyStructure(Structure.StructureSettings config,
                                  Holder<StructureTemplatePool> startPool,
                                  Optional<ResourceLocation> startJigsawName,
                                  int size,
                                  HeightProvider startHeight,
                                  Optional<Heightmap.Types> projectStartToHeightmap,
                                  int maxDistanceFromCenter)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor heightAccessor = context.heightAccessor();
        RandomState randomState = context.randomState();

        // A single noise column replaces the two separate samples of the same column that were taken before
        // (MOTION_BLOCKING_NO_LEAVES and WORLD_SURFACE_WG are identical for raw noise columns), so the start height -
        // and therefore the position of every structure that already generated - is unchanged.
        NoiseColumn cornerColumn = generator.getBaseColumn(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), heightAccessor, randomState);
        int startY = surfaceY(cornerColumn, heightAccessor);
        if (startY >= MAX_START_HEIGHT || isFluid(cornerColumn, startY)) {
            return Optional.empty();
        }

        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), startY + 2, chunkPos.getMinBlockZ());

        Optional<Structure.GenerationStub> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        context,
                        this.startPool,
                        this.startJigsawName,
                        this.size,
                        blockPos,
                        false,
                        Optional.empty(), // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
                        this.maxDistanceFromCenter); // Maximum limit for how far pieces can spawn from center. You cannot set this bigger than 128 or else pieces gets cutoff.

        if (structurePiecesGenerator.isEmpty()) {
            return structurePiecesGenerator;
        }

        // The pieces themselves are only assembled later (lazily), so the checks below are cheap and do not
        // consume any randomness: accepted structures generate exactly as before.
        BlockPos center = structurePiecesGenerator.get().position();
        if (!isValidBiome(context, center)
                || isOnOpenWater(generator, heightAccessor, randomState, center)
                || (DivineRelicsCommonConfig.NO_WATER_NEARBY_USE.get() && !isFarFromWaterBiomes(context, center.getX(), startY, center.getZ()))) {
            return Optional.empty();
        }
        return structurePiecesGenerator;
    }

    /**
     * The same biome test {@code Structure#generate} performs after this method (1.19.3+ does it here as well).
     * Rejecting early lets {@code /locate}, the Dwarven Compass and the structure check skip chunk loads for starts
     * that would be discarded anyway.
     */
    private static boolean isValidBiome(Structure.GenerationContext context, BlockPos pos) {
        Holder<Biome> biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pos.getX()),
                QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ()), context.randomState().sampler());
        return context.validBiome().test(biome);
    }

    /**
     * Rejects starts whose surface pieces would stand in a river, lake or ocean: samples a 3x3 grid of raw noise
     * columns across the start piece and checks whether the topmost block is a fluid.
     */
    private static boolean isOnOpenWater(ChunkGenerator generator, LevelHeightAccessor heightAccessor, RandomState randomState, BlockPos center) {
        for (int dx = -SURFACE_CHECK_RADIUS; dx <= SURFACE_CHECK_RADIUS; dx += SURFACE_CHECK_RADIUS) {
            for (int dz = -SURFACE_CHECK_RADIUS; dz <= SURFACE_CHECK_RADIUS; dz += SURFACE_CHECK_RADIUS) {
                NoiseColumn column = generator.getBaseColumn(center.getX() + dx, center.getZ() + dz, heightAccessor, randomState);
                if (isFluid(column, surfaceY(column, heightAccessor))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Optional stricter check (config "Use NoWaterNearby structure type"): no water biome within the configured
     * distance. Samples a 2D disc at surface height with early exit - the previous implementation sampled a full 3D
     * cube at y = 0 (cave biomes, not the surface) and printed every biome to stdout.
     */
    private static boolean isFarFromWaterBiomes(Structure.GenerationContext context, int x, int y, int z) {
        int radius = QuartPos.fromBlock(Math.max(0, DivineRelicsCommonConfig.NO_WATER_NEARBY_DISTANCE.get()));
        int radiusSqr = radius * radius;
        int quartX = QuartPos.fromBlock(x);
        int quartY = QuartPos.fromBlock(y);
        int quartZ = QuartPos.fromBlock(z);
        BiomeSource biomeSource = context.chunkGenerator().getBiomeSource();
        Climate.Sampler sampler = context.randomState().sampler();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radiusSqr && isWaterBiome(biomeSource.getNoiseBiome(quartX + dx, quartY, quartZ + dz, sampler))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isWaterBiome(Holder<Biome> biome) {
        return biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_BEACH)
                || biome.unwrapKey().filter(WATER_BIOME_KEYS::contains).isPresent();
    }

    /** Y of the topmost non-air block, i.e. {@code getFirstOccupiedHeight(WORLD_SURFACE_WG)}. */
    private static int surfaceY(NoiseColumn column, LevelHeightAccessor heightAccessor) {
        for (int y = heightAccessor.getMaxBuildHeight() - 1; y >= heightAccessor.getMinBuildHeight(); y--) {
            if (!column.getBlock(y).isAir()) {
                return y;
            }
        }
        return heightAccessor.getMinBuildHeight() - 1;
    }

    private static boolean isFluid(NoiseColumn column, int y) {
        return !column.getBlock(y).getFluidState().isEmpty();
    }

    @Override
    public StructureType<?> type() {
        return DRStructures.NO_WATER_NEARBY.get();
    }
}
