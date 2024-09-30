package com.deriys.divinerelics.structures;

import com.deriys.divinerelics.init.DRStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NoWaterNearbyStructure extends Structure {

    public static final Codec<NoWaterNearbyStructure> CODEC = RecordCodecBuilder.<NoWaterNearbyStructure>mapCodec(instance ->
            instance.group(NoWaterNearbyStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
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

    private static boolean extraSpawningChecks(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ());

        // Check if the structure is far from water biomes
        if (!isFarFromWaterBiomes(context, blockPos)) {
            return false;
        }

        return context.chunkGenerator().getFirstOccupiedHeight(
                chunkPos.getMinBlockX(),
                chunkPos.getMinBlockZ(),
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                context.heightAccessor(),
                context.randomState()) < 90;
    }

    private static boolean isFarFromWaterBiomes(Structure.GenerationContext context, BlockPos pos) {
        Set<Holder<Biome>> biomes = context.biomeSource().getBiomesWithin(pos.getX(), pos.getY(), pos.getZ(), 40, context.randomState().sampler());
        for (Holder<Biome> biomeHolder : biomes) {
            Biome biome = biomeHolder.value();
            ResourceLocation biomeName = context.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
            if (biomeName != null && WATER_BIOMES.contains(biomeName.toString())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {

        if (!NoWaterNearbyStructure.extraSpawningChecks(context)) {
            return Optional.empty();
        }

        int startY = context.chunkGenerator().getFirstOccupiedHeight(
                context.chunkPos().getMinBlockX(),
                context.chunkPos().getMinBlockZ(),
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState());

        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), startY, chunkPos.getMinBlockZ());

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

        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return DRStructures.NO_WATER_NEARBY.get();
    }
}
