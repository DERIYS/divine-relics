package com.deriys.tutorialmod.items;

import com.deriys.tutorialmod.effects.ModEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class HeimdallGauntlet extends Item {

    public HeimdallGauntlet(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.addEffect(new MobEffectInstance(ModEffects.BIFROST_PROTECTION.get(), 200, 0));
//        if (interactionHand == InteractionHand.MAIN_HAND) {
//            player.getCooldowns().addCooldown(this, 1200);
//        }
//        else {
//            player.getCooldowns().addCooldown(this, 1400);
//        }
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    public static double getRandomPos(double X) {
        Random rand = new Random();
        float result = rand.nextFloat() * 4 - 2;
        return X + result;
    }

    public static void addTeleportParticles(Level level, double pX, double pY, double pZ, double tpX, double tpZ) {
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {
           double rX = rand.nextFloat() - 0.5;
           double rZ = rand.nextFloat() - 0.5;

            level.addParticle(ParticleTypes.PORTAL,
                    pX + rX,
                    pY + (double) i /97,
                    pZ + rZ,
                    0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.PORTAL,
                    tpX + rX,
                    pY + (double) i /97,
                    tpZ + rZ,
                    0.0D, 0.0D, 0.0D);
        }
    }
}
