package com.deriys.divinerelics.structures;

import com.deriys.divinerelics.init.DRStructures;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

/**
 * Keeps world fluids out of a structure piece while it is being placed.
 * <p>
 * Underground jigsaw pieces with {@code beard_box} terrain adaptation have their bounding boxes (plus a margin) carved
 * out during the noise stage, and the aquifer fills every carved cell below the local fluid level with water.
 * Vanilla template placement keeps liquids ({@code keepLiquids = true}), so every waterloggable template block
 * (fences, stairs, slabs, rails, chains...) placed into that water - or next to a water source - becomes a
 * waterlogged <i>source block</i> with a scheduled fluid tick. The Dwarven Mines templates contain hundreds of such
 * blocks, which is why whole mines generated flooded, especially near rivers where the fluid level is high.
 * <p>
 * For every template cell inside the chunk that is currently being placed, this processor:
 * <ol>
 *     <li>removes the world fluid where a waterloggable block is about to go, so it is not waterlogged;</li>
 *     <li>replaces fluid in the cells water could flow in from (above and the four horizontal sides) of every open or
 *     waterloggable cell with stone, sealing the piece against the carved aquifer water, rivers and lakes.</li>
 * </ol>
 * Fluids that belong to the template itself are never touched: those cells are skipped, and the processing of all
 * blocks happens before any of them is placed, so the template always overwrites the temporary seal blocks.
 */
public class FluidSealProcessor extends StructureProcessor {
    public static final FluidSealProcessor INSTANCE = new FluidSealProcessor();
    public static final Codec<FluidSealProcessor> CODEC = Codec.unit(() -> INSTANCE);

    /** Directions a fluid can enter a cell from: falling from above or spreading sideways (never upwards). */
    private static final Direction[] INFLOW_DIRECTIONS = {Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    private FluidSealProcessor() {
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos pivot,
                                                        StructureTemplate.StructureBlockInfo rawInfo,
                                                        StructureTemplate.StructureBlockInfo info,
                                                        StructurePlaceSettings settings,
                                                        @Nullable StructureTemplate template) {
        BlockState state = info.state;
        BoundingBox box = settings.getBoundingBox();
        BlockPos pos = info.pos;
        // Only touch the world while a template is really being placed, and only around cells placed by this pass;
        // cells outside the box belong to a neighbouring chunk and are handled when that chunk is placed.
        if (!(level instanceof LevelAccessor world) || !state.getFluidState().isEmpty() || (box != null && !box.isInside(pos))) {
            return info;
        }

        boolean waterloggable = state.hasProperty(BlockStateProperties.WATERLOGGED) || state.getBlock() instanceof LiquidBlockContainer;
        if (waterloggable && !world.getFluidState(pos).isEmpty()) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }

        if (waterloggable || !state.getMaterial().blocksMotion()) {
            BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
            for (Direction direction : INFLOW_DIRECTIONS) {
                neighbor.setWithOffset(pos, direction);
                // Neighbours are at most one block outside the chunk, which world generation allows writing to.
                FluidState fluid = world.getFluidState(neighbor);
                if (!fluid.isEmpty()) {
                    seal(world, neighbor);
                }
            }
        }
        return info;
    }

    private static void seal(LevelAccessor world, BlockPos pos) {
        BlockState current = world.getBlockState(pos);
        if (current.hasProperty(BlockStateProperties.WATERLOGGED)) {
            world.setBlock(pos, current.setValue(BlockStateProperties.WATERLOGGED, false), Block.UPDATE_CLIENTS);
        } else {
            world.setBlock(pos, (pos.getY() < 0 ? Blocks.DEEPSLATE : Blocks.STONE).defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return DRStructures.FLUID_SEAL.get();
    }
}
