package com.deriys.divinerelics.items;

import com.deriys.divinerelics.init.DREffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

import static com.deriys.divinerelics.items.Motosignir.gainMobEffects;

public class HeimdallGauntlet extends SwordItem {

    public static final Random RAND = new Random();
    public static final double VECTOR_ANGLE_CONSTANT = 3 * Math.PI / 2 - Math.PI / 4;
    private final MobEffect[] EFFECTS = {
            DREffects.BIFROST_PROTECTION.get(),
            MobEffects.MOVEMENT_SPEED
    };

    private final int EFFECTS_DURATION = 300;
    private final int AMPLIFIER = 0;

    public HeimdallGauntlet(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.literal("The favorite gauntlet of the Aesir god Heimdall, endowed with his Bifrost Power."));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            gainMobEffects(player, EFFECTS, EFFECTS_DURATION, AMPLIFIER);
            if (interactionHand == InteractionHand.MAIN_HAND) {
                player.getCooldowns().addCooldown(this, 1200);
            }
            else {
                player.getCooldowns().addCooldown(this, 1400);
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
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
//        public static void drawTPArea(Level level, Vec2 normVector, Vec3 entityPos, double length) {
//            for (int i = -45; i <= 225; i++) {
//                double vectorAngle = Math.PI * i / 180;
//
//                Vec3 vector = rotateVector(normVector, vectorAngle, length);
//                for (int j = 0; j <= 10; j++) {
//                    Vec3 end = entityPos.add(vector.scale((double) j / 10));
//
//                    level.addParticle(ParticleTypes.FLAME, end.x, entityPos.y + 1, end.z, 0.0D, 0.0D, 0.0D);
//                }
//            }
//        }
}
