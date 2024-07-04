package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBinding;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.capabilities.teammates.Teammates;
import com.deriys.divinerelics.capabilities.teammates.TeammatesProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.GauntletParticleS2CPacket;
import com.deriys.divinerelics.init.DREffects;
import com.deriys.divinerelics.entities.entity.ThrownDraupnirSpear;
import com.deriys.divinerelics.entities.entity.ThrownMjolnir;
import com.deriys.divinerelics.init.DRItems;
import com.deriys.divinerelics.items.DraupnirSpear;
import com.deriys.divinerelics.items.Mjolnir;
import com.deriys.divinerelics.items.Motosignir;
import com.deriys.divinerelics.init.DRSounds;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;
import static com.deriys.divinerelics.items.DraupnirSpear.RAND;
import static com.deriys.divinerelics.items.HeimdallGauntlet.*;


public class DREvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void LivingAttackEvent(LivingAttackEvent event) {
            Entity hurtEntity = event.getEntity();
            Entity attacker = event.getSource().getEntity();
            Entity directAttacker = event.getSource().getDirectEntity();

            if (hurtEntity instanceof LivingEntity livingEntity) {
                Level level = livingEntity.getLevel();
                if (livingEntity.hasEffect(DREffects.BIFROST_PROTECTION.get())) {
                    if (isValidAttacker(attacker, directAttacker)) {
                        Vec3 entityPos = livingEntity.position();
                        Vec3 attackerPos = attacker.position();

                        Vec2 attackVector = new Vec2((float) (entityPos.x - attackerPos.x), (float) (entityPos.z - attackerPos.z));
                        dodgeAttack(level, livingEntity, attackVector);
                        hurtEntity.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.getEyePosition());
                        event.setCanceled(true);
                    }
                }
            }

            if (hurtEntity instanceof Player player && attacker instanceof LivingEntity livingAttacker) {
                if (player.isBlocking() && player.getOffhandItem().getItem() == DRItems.GUARDIAN_SHIELD.get()) {
                    int ticksInUse = player.getTicksUsingItem();
                    if (ticksInUse <= 12) {
                        float amount = event.getAmount();
                        float damage = (amount < 3)? 2: amount / 1.2f;
                        player.getLevel().playSound(null, player.getOnPos(), DRSounds.GUARDIAN_SHIELD_PARRY.get(), SoundSource.PLAYERS, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
                        Motosignir.hurtAndKnockbackEntites(List.of(livingAttacker), player, Motosignir.NEGATIVE_EFFECTS, damage, Math.min(Math.log10(damage) / 2.5, 1.0f), 1, 100);
                    }
                }
            }

            if (hurtEntity instanceof LivingEntity && attacker instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.getItem() instanceof Mjolnir mjolnir && !player.getLevel().isClientSide) {
                    if (mjolnir.isRiptideFlying(itemStack)) {
                        player.getCooldowns().addCooldown(mjolnir, 40);
                    }
                }
            }

            if (hurtEntity instanceof LivingEntity livingEntityHurt && directAttacker instanceof ThrownMjolnir thrownMjolnir && thrownMjolnir.getOwner() instanceof LivingEntity owner && owner == livingEntityHurt) {
                event.setCanceled(true);
            }

            if (hurtEntity instanceof LivingEntity hurtPlayer && attacker instanceof Player attackerPlayer && hasTeammate(attackerPlayer, hurtPlayer) && isDRWeapon(directAttacker)) {
                event.setCanceled(true);
            }

        }

        @SubscribeEvent
        public static void onShieldBlock(ShieldBlockEvent event) {
            LivingEntity entity = event.getEntity();
            Entity attacker = event.getDamageSource().getDirectEntity();
            if (attacker instanceof ThrownMjolnir || attacker instanceof ThrownDraupnirSpear) {
                if (entity.getOffhandItem().getItem() != DRItems.GUARDIAN_SHIELD.get()) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerFall(LivingFallEvent event) {
            if (event.getEntity() instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                Level level = player.getLevel();
                if (itemStack.getItem() instanceof Mjolnir mjolnir && !level.isClientSide) {
                    if (mjolnir.isRiptideFlying(itemStack)) {
                        if (event.getDistance() > 20) {
                            double velocity = getVelocity(player);
                            float damage = ((float) Math.min(Math.max(10f, 5f * velocity), 40f));
                            float force = (float) (1 + velocity / (velocity + 0.5));
                            DamageSource damageSource = DamageSource.trident(new ThrownMjolnir(level, player, itemStack), player);

                            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 80, 2));
                            ThrownMjolnir.onHitLighnting(event.getEntity().getLevel(), player, player.getOnPos(), player, damageSource, damage, force, 5);

                            player.getCooldowns().addCooldown(mjolnir, 70);
                        }
                        player.startAutoSpinAttack(1);
                        mjolnir.setRiptideFlying(itemStack, false);
                        event.setCanceled(true);
                    }

                }
            }
        }

        @SubscribeEvent
        public static void onLightningStrike(EntityStruckByLightningEvent event) {
            ServerPlayer cause = event.getLightning().getCause();
            if(event.getEntity() instanceof LivingEntity hitEntity && cause != null && (cause == hitEntity || hasTeammate(cause, hitEntity))) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "mjolnirbinding"), new MjolnirBindingProvider());
                }
                if (!event.getObject().getCapability(TeammatesProvider.TEAMMATES).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "drteammates"), new TeammatesProvider());
                }

            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(MjolnirBinding.class);
            event.register(Teammates.class);
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if(event.isWasDeath()) {
                Player original = event.getOriginal();
                original.revive();

                original.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(oldStore -> {
                    event.getEntity().getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });

                original.getCapability(TeammatesProvider.TEAMMATES).ifPresent(oldStore -> {
                    event.getEntity().getCapability(TeammatesProvider.TEAMMATES).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onItemPickup(EntityItemPickupEvent event) {
            ItemStack itemStack = event.getItem().getItem();
            Item item = itemStack.getItem();
            String owner = getOwner(itemStack);
            Player player = event.getEntity();

            if (isValidBindingItem(item) && owner.isEmpty()) {
                bindItemToEntity(player, itemStack);
            } else if (!owner.isEmpty()) {
                if (!owner.equals(player.getUUID().toString())) {
                    event.setCanceled(true);
                }
            }
        }

        public static boolean isValidBindingItem(Item item) {
            return item instanceof Mjolnir || item instanceof DraupnirSpear;
        }

        public static void bindItemToEntity(Entity entity, ItemStack itemStack) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            nbt.putString("OwnerUUID", entity.getUUID().toString());
            nbt.putString("OwnerNickname", entity.getDisplayName().getString());
        }

        public static String getOwner(ItemStack itemStack) {
            if (itemStack != null) {
                CompoundTag nbt = itemStack.getOrCreateTag();
                return nbt.getString("OwnerUUID");
            }
            return "";
        }

        public static double getVelocity(Player player) {
            double deltaX = player.getX() - player.xOld;
            double deltaY = player.getY() - player.yOld;
            double deltaZ = player.getZ() - player.zOld;
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        }

        private static boolean isValidAttacker(Entity attacker, Entity directAttacker) {
            return !(directAttacker instanceof ThrownDraupnirSpear) && (attacker instanceof LivingEntity livingEntity && livingEntity.getMainHandItem().getItem() != DRItems.DRAUPNIR_SPEAR.get());
        }

        public static boolean isDRWeapon(Entity directAttacker) {
            return directAttacker instanceof ThrownMjolnir || directAttacker instanceof ThrownDraupnirSpear;
        }

        public static void dodgeAttack(Level level, LivingEntity hurtEntity, Vec2 attackVector) {
            Vec3 entityPos = hurtEntity.position();

            double entityX = hurtEntity.getX();
            double entityY = hurtEntity.getY();
            double entityZ = hurtEntity.getZ();

            if (hurtEntity.isPassenger()) { hurtEntity.stopRiding(); }

            Vec2 attackVNorm = findNormVec(attackVector);

            double scale = new Random().nextFloat() * 1.5 + 0.5;

            Vec3 tpVector = getTPVector(level, attackVNorm, entityPos, scale);

            hurtEntity.teleportTo(tpVector.x, tpVector.y, tpVector.z);

            if (level instanceof ServerLevel) {
                GauntletParticleS2CPacket packet = new GauntletParticleS2CPacket(entityX, entityY, entityZ, tpVector.x, tpVector.z);
                DRMessages.sendToChunk(packet, level.getChunkAt(hurtEntity.blockPosition()));
            }

            level.playSound(null, entityX, entityY, entityZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1f, 1f);
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
}
