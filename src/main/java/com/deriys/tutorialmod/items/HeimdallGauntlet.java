package com.deriys.tutorialmod.items;

import com.deriys.tutorialmod.effects.ModEffects;
import com.deriys.tutorialmod.items.custom.TPData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

import static com.deriys.tutorialmod.items.Motosignir.gainMobEffects;

public class HeimdallGauntlet extends SwordItem {

    private static final Random RAND = new Random();
    public static final double VECTOR_ANGLE_CONSTANT = 3 * Math.PI / 2 - Math.PI / 4;
    private final MobEffect[] EFFECTS = {
            ModEffects.BIFROST_PROTECTION.get(),
            MobEffects.MOVEMENT_SPEED
    };

    private final int EFFECTS_DURATION = 300;
    private final int AMPLIFIER = 0;

    public HeimdallGauntlet(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("The favorite gauntlet of the Aesir god Heimdall, endowed with his Bifrost Power"));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        gainMobEffects(player, EFFECTS, EFFECTS_DURATION, AMPLIFIER);
//        if (interactionHand == InteractionHand.MAIN_HAND) {
//            player.getCooldowns().addCooldown(this, 1200);
//        }
//        else {
//            player.getCooldowns().addCooldown(this, 1400);
//        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    public static Vec2 findNormVec(Vec2 vector) {
        double x = vector.x;
        double z = vector.y;

        double zn = x / (Math.sqrt(x*x + z*z));
        double xn = -z*zn/x;

        return new Vec2((float) xn, (float) zn);
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
        Vec3 tpVector = null;
        for (int i = 0; i < 5; i++) {
            double vectorAngle = RAND.nextFloat() * VECTOR_ANGLE_CONSTANT;
            Vec3 vector = rotateVector(normVector, vectorAngle, scale);

            // setting the teleportation vector
            tpVector = entityPos.add(vector);

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

    public static void addTeleportParticles(Level level, double pX, double pY, double pZ, double tpX, double tpZ) {
        for (int i = 0; i < 70; i++) {
           double rX = RAND.nextFloat() - 0.5;
           double rZ = RAND.nextFloat() - 0.5;

            level.addParticle(ParticleTypes.PORTAL,
                    pX + rX,
                    pY + (double) i / 97,
                    pZ + rZ,
                    0, 0, 0);
            level.addParticle(ParticleTypes.PORTAL,
                    tpX + rX,
                    pY + (double) i / 97,
                    tpZ + rZ,
                    0, 0, 0);
        }
    }
}
