package com.deriys.divinerelics.effects;

import com.deriys.divinerelics.util.custom.TPData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class BifrostProtection extends MobEffect {
    public static final Random RAND = new Random();
    public static final double VECTOR_ANGLE_CONSTANT = 3 * Math.PI / 2 - Math.PI / 4;

    public BifrostProtection(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        super.applyEffectTick(livingEntity, pAmplifier);
    }

    public static Vec2 findNormVec(Vec2 vector) {
        float x = vector.x;
        float z = vector.y;

        float zn = (float) (x / (Math.sqrt(x*x + z*z)));
        float xn = -z*zn/x;

        return new Vec2(xn, zn);
    }

    public static Vec2 findNormVec(Vec3 vector) {
        return findNormVec(new Vec2((float) vector.x, (float) vector.y));
    }

    public static Vec3 rotateVector(Vec2 normVector, double vectorAngle) {
        double baseAngle = Math.atan2(normVector.y, normVector.x);
        double angle = baseAngle + vectorAngle;

        return new Vec3(-Math.cos(angle), 0, -Math.sin(angle));
    }

    public static Vec3 rotateVector(Vec2 normVector, double vectorAngle, double length) {
        double baseAngle = Math.atan2(normVector.y, normVector.x);
        double angle = baseAngle + vectorAngle;

        return new Vec3(-Math.cos(angle), 0, -Math.sin(angle)).scale(length);
    }

    public static Vec3 getTPVector(Level level, Vec2 normVector, Vec3 entityPos, double scale) {
        for (int i = 0; i < 5; i++) {
            double vectorAngle = RAND.nextFloat() * VECTOR_ANGLE_CONSTANT;
            Vec3 vector = rotateVector(normVector, vectorAngle, scale);

            // setting the teleportation vector
            Vec3 tpVector = entityPos.add(vector);

            TPData isSafe = isSafeTP(level, tpVector);

            if (isSafe.getStatus()) {
                return tpVector.add(new Vec3(0, isSafe.getOffset(), 0));
            }
        }
        // if unsafe > 5 times
        BlockPos pos = new BlockPos(entityPos);
        return new Vec3(pos.getX() + RAND.nextFloat() * 0.4 + 0.3, entityPos.y, pos.getZ() + RAND.nextFloat() * 0.4 + 0.3);
    }

    public static TPData isSafeTP(Level level, Vec3 tpVector) {
        BlockPos middle = new BlockPos(tpVector);
        BlockPos ground = middle.below();
        BlockPos below = ground.below();
        BlockPos above = middle.above();
        BlockPos above1 = above.above();

        boolean isMiddleEmpty = isCollEmpty(level, level.getBlockState(middle), middle);
        boolean isAboveEmpty = isCollEmpty(level, level.getBlockState(above), above);
        boolean isAbove1Empty = isCollEmpty(level, level.getBlockState(above1), above1);
        boolean isGroundEmpty = isCollEmpty(level, level.getBlockState(ground), ground);
        boolean isBelowEmpty = isCollEmpty(level, level.getBlockState(below), below);

        if (isMiddleEmpty && isAboveEmpty) {
            if (!isGroundEmpty) {
                return new TPData(0, true);
            } else if (!isBelowEmpty){
                return new TPData(-1, true);
            }
        } else if (isAboveEmpty && isAbove1Empty && level.getBlockState(middle).getBlock() != Blocks.FIRE) {
            return new TPData(1, true);
        }

        return new TPData(0, false);
    }

    public static boolean isCollEmpty(Level level, BlockState blockState, BlockPos blockPos) {
        return blockState.getCollisionShape(level, blockPos).isEmpty() && blockState.getBlock() != Blocks.FIRE;
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return true;
    }
}
