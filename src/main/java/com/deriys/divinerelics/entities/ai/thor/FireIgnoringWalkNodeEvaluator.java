package com.deriys.divinerelics.entities.ai.thor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class FireIgnoringWalkNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public BlockPathTypes getBlockPathType(BlockGetter p_77576_, int p_77577_, int p_77578_, int p_77579_) {
        BlockPathTypes type = super.getBlockPathType(p_77576_, p_77577_, p_77578_, p_77579_);
        if (type.equals(BlockPathTypes.DAMAGE_FIRE) || type.equals(BlockPathTypes.DANGER_FIRE)) {
            return BlockPathTypes.WALKABLE;
        } return type;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter p_77594_, int p_77595_, int p_77596_, int p_77597_, Mob p_77598_, int p_77599_, int p_77600_, int p_77601_, boolean p_77602_, boolean p_77603_) {
        BlockPathTypes type = super.getBlockPathType(p_77594_, p_77595_, p_77596_, p_77597_, p_77598_, p_77599_, p_77600_, p_77601_, p_77602_, p_77603_);
        if (type.equals(BlockPathTypes.DAMAGE_FIRE) || type.equals(BlockPathTypes.DANGER_FIRE)) {
            return BlockPathTypes.WALKABLE;
        } return type;
    }

    @Override
    public BlockPathTypes getBlockPathTypes(BlockGetter p_77581_, int p_77582_, int p_77583_, int p_77584_, int p_77585_, int p_77586_, int p_77587_, boolean p_77588_, boolean p_77589_, EnumSet<BlockPathTypes> p_77590_, BlockPathTypes p_77591_, BlockPos p_77592_) {
        BlockPathTypes type = super.getBlockPathTypes(p_77581_, p_77582_, p_77583_, p_77584_, p_77585_, p_77586_, p_77587_, p_77588_, p_77589_, p_77590_, p_77591_, p_77592_);
        if (type.equals(BlockPathTypes.DAMAGE_FIRE) || type.equals(BlockPathTypes.DANGER_FIRE)) {
            return BlockPathTypes.WALKABLE;
        } return type;
    }
}
