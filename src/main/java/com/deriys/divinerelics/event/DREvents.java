package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.leviathan.LeviathanBinding;
import com.deriys.divinerelics.capabilities.leviathan.LeviathanBindingProvider;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBinding;
import com.deriys.divinerelics.capabilities.mjolnir.MjolnirBindingProvider;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpears;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.capabilities.teammates.Teammates;
import com.deriys.divinerelics.capabilities.teammates.TeammatesProvider;
import com.deriys.divinerelics.capabilities.thor.FightsThor;
import com.deriys.divinerelics.capabilities.thor.FightsThorProvider;
import com.deriys.divinerelics.entities.entity.*;
import com.deriys.divinerelics.init.*;
import com.deriys.divinerelics.entities.ai.thor.ThorShieldKnockback;
import com.deriys.divinerelics.items.DraupnirSpear;
import com.deriys.divinerelics.items.GuardianShield;
import com.deriys.divinerelics.items.LeviathanAxe;
import com.deriys.divinerelics.items.Mjolnir;
import com.deriys.divinerelics.items.Motosignir;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.*;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;
import static com.deriys.divinerelics.effects.BifrostProtection.findNormVec;
import static com.deriys.divinerelics.effects.BifrostProtection.getTPVector;
import static com.deriys.divinerelics.entities.entity.ThorEntity.stopSoundForPlayer;
import static com.deriys.divinerelics.items.DraupnirSpear.RAND;


public class DREvents {
    @Mod.EventBusSubscriber(modid = DivineRelics.MODID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void LivingAttackEvent(LivingAttackEvent event) {
            Entity hurtEntity = event.getEntity();
            DamageSource damageSource = event.getSource();
            Entity attacker = damageSource.getEntity();
            Entity directAttacker = damageSource.getDirectEntity();

            // Attacks that are always ignored are resolved first, so they no longer trigger side effects below
            // (Bifrost dodge teleport, Guardian Shield parry) before being cancelled anyway.
            if (hurtEntity instanceof LivingEntity livingEntityHurt) {
                if (directAttacker instanceof ThrownMjolnir thrownMjolnir && thrownMjolnir.getOwner() == livingEntityHurt) {
                    event.setCanceled(true);
                    return;
                }
                if (attacker instanceof Player attackerPlayer && isDRWeapon(directAttacker) && hasTeammate(attackerPlayer, livingEntityHurt)) {
                    event.setCanceled(true);
                    return;
                }
            }

            if (hurtEntity != null && attacker instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                if (itemStack.getItem() instanceof Mjolnir mjolnir && !player.getLevel().isClientSide) {
                    if (mjolnir.isRiptideFlying(itemStack)) {
                        if (!hasTeammate(player, ((LivingEntity) hurtEntity))) {
                            player.getCooldowns().addCooldown(mjolnir, 40);
                        } else {
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }

            if (hurtEntity instanceof LivingEntity livingEntity && !livingEntity.getLevel().isClientSide
                    && canBifrostDodge(livingEntity, attacker, directAttacker)) {
                bifrostDodge(livingEntity, attacker, livingEntity.position().subtract(attacker.position()));
                event.setCanceled(true);
            }

            if (!isParrying && hurtEntity instanceof Player player && !player.getLevel().isClientSide && attacker instanceof LivingEntity livingAttacker) {
                if (GuardianShield.isParrying(player)) {
                    float amount = event.getAmount();
                    float damage = (amount < 3)? 2: amount * 0.8f;
                    player.getLevel().playSound(null, player.getOnPos(), DRSounds.GUARDIAN_SHIELD_PARRY.get(), SoundSource.PLAYERS, 1.0f, RAND.nextFloat() * 0.1F + 0.95F);
                    // The parry damages the attacker, which fires this event again. If the attacker is parrying as
                    // well, both players would parry each other forever (StackOverflowError on the server thread).
                    isParrying = true;
                    try {
                        Motosignir.hurtAndKnockbackEntites(List.of(livingAttacker), player, Motosignir.STUN_EFFECTS, damage, Math.min(Math.log10(damage) / 2.5, 1.5f), 1, 100);
                    } finally {
                        isParrying = false;
                    }
                }
            }
        }

        /** Re-entrancy guard for the Guardian Shield parry (only touched on the server thread). */
        private static boolean isParrying = false;


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

        /** Runs last, so it only sees blocks that really happen: Thor's attacks still push through those. */
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onShieldBlocked(ShieldBlockEvent event) {
            if (event.getBlockedDamage() > 0.0F) {
                ThorShieldKnockback.onShieldBlock(event.getEntity());
            }
        }

        @SubscribeEvent
        public static void onPlayerFall(LivingFallEvent event) {
            if (event.getEntity() instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();
                Level level = player.getLevel();
                if (!level.isClientSide) {
                    if (itemStack.getItem() instanceof Mjolnir mjolnir) {
                        if (mjolnir.isRiptideFlying(itemStack)) {
                            handleMjolnirAction(player, mjolnir, itemStack, event);
                        }
                        mjolnir.setRiptideFlying(itemStack, false);
                    } else if (player.getUseItem().is(DRItems.GUARDIAN_SHIELD.get()) && player.getLookAngle().y < -0.85D) {
                        handleGuardianShieldAction(player, event);
                    }
                }
            }
        }

        private static void handleGuardianShieldAction(Player player, LivingFallEvent event) {
            if (player.isAutoSpinAttack()) player.startAutoSpinAttack(1);
            event.setCanceled(true);
        }

        private static void handleMjolnirAction(Player player, Mjolnir mjolnir, ItemStack itemStack, LivingFallEvent event) {
            if (event.getDistance() > 20) {
                applyMjolnirEffects(player, mjolnir, itemStack, event);
            }
            if (player.isAutoSpinAttack()) player.startAutoSpinAttack(1);
            event.setCanceled(true);
        }

        private static void applyMjolnirEffects(Player player, Mjolnir mjolnir, ItemStack itemStack, LivingFallEvent event) {
            double velocity = getVelocity(player);
            float damage = ((float) Math.min(Math.max(10f, 5f * velocity), 40f));
            float force = (float) (1 + velocity / (velocity + 0.5));
            DamageSource damageSource = DamageSource.trident(new ThrownMjolnir(player.getLevel(), player, itemStack), player);

            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 80, 2));
            ThrownMjolnir.onHitLighnting(event.getEntity().getLevel(), player, player.getOnPos(), player, damageSource, damage, force, 5);

            player.getCooldowns().addCooldown(mjolnir, 70);
        }

        @SubscribeEvent
        public static void onLightningStrike(EntityStruckByLightningEvent event) {
            ServerPlayer cause = event.getLightning().getCause();
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity hitEntity) {
                if (cause != null && (cause == hitEntity || hasTeammate(cause, hitEntity)) || entity instanceof ThorEntity || hitEntity.isBlocking()) {
                    event.setCanceled(true);
                }
            }
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getItem().is(DRTags.Items.CANT_BE_DESTROYED_BY_LIGHTNING)) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof LivingEntity livingEntity) {
                if (!livingEntity.getCapability(StuckSpearsProvider.STUCK_SPEARS).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "stuck_spears"), new StuckSpearsProvider());
                }
            }

            if (event.getObject() instanceof Player player) {
                if (!player.getCapability(MjolnirBindingProvider.MJOLNIR_BINDING).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "mjolnirbinding"), new MjolnirBindingProvider());
                }
                if (!player.getCapability(TeammatesProvider.TEAMMATES).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "drteammates"), new TeammatesProvider());
                }
                if (!player.getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "leviathanbinding"), new LeviathanBindingProvider());
                }
                if (!player.getCapability(FightsThorProvider.FIGHTS_THOR).isPresent()) {
                    event.addCapability(new ResourceLocation(DivineRelics.MODID, "fightsthor"), new FightsThorProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(MjolnirBinding.class);
            event.register(Teammates.class);
            event.register(LeviathanBinding.class);
            event.register(StuckSpears.class);
            event.register(FightsThor.class);
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            Player original = event.getOriginal();
            Player player = event.getEntity();
            // A new player object is also created when leaving the End, not only on death: the capabilities have to be
            // carried over in both cases (before, returning from the End wiped teammates and weapon bindings).
            // reviveCaps()/invalidateCaps() is the Forge pattern for reading the removed player's capabilities;
            // revive() also un-removed the dead entity itself.
            original.reviveCaps();
            try {
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

                original.getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).ifPresent(oldStore -> {
                    event.getEntity().getCapability(LeviathanBindingProvider.LEVIATHAN_BINDING).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });

                original.getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(oldStore -> {
                    event.getEntity().getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            } finally {
                original.invalidateCaps();
            }

            // The death position is only recorded on death; for other clones it would read (0, 0, 0).
            if (!event.isWasDeath() || !original.getPersistentData().contains("DRdeathPosition", Tag.TAG_COMPOUND)
                    || !(player instanceof ServerPlayer)) {
                return;
            }

            Objects.requireNonNull(player.getServer()).execute(() -> {
                CompoundTag deathPositionTag = original.getPersistentData().getCompound("DRdeathPosition");
                double deathX = deathPositionTag.getDouble("x");
                double deathY = deathPositionTag.getDouble("y");
                double deathZ = deathPositionTag.getDouble("z");
                BlockPos deathPos = new BlockPos(deathX, deathY, deathZ);

                double distanceSqr = player.position().distanceToSqr(deathPos.getX(), deathPos.getY(), deathPos.getZ());

                if (distanceSqr > 70000) {
                    stopSoundForPlayer((ServerPlayer) player, DRSounds.THOR_FIGHT_MUSIC.get(), SoundSource.RECORDS);
                    stopSoundForPlayer((ServerPlayer) player, DRSounds.THOR_FIGHT_MUSIC_LOOP.get(), SoundSource.RECORDS);
                    setLoggedOutCap(player);
                }
            });
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if (event.getEntity() instanceof Player player) {
                CompoundTag deathPositionTag = new CompoundTag();
                deathPositionTag.putDouble("x", player.getX());
                deathPositionTag.putDouble("y", player.getY());
                deathPositionTag.putDouble("z", player.getZ());
                player.getPersistentData().put("DRdeathPosition", deathPositionTag);
            }
        }

        @SubscribeEvent
        public static void onItemPickup(EntityItemPickupEvent event) {
            ItemStack itemStack = event.getItem().getItem();
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tag.isEmpty()) {
                // Repairs stacks that older versions tagged with an empty "{}" on pickup (see getOwner), which made
                // them refuse to stack with identical untagged items. Vanilla normalises empty tags to null as well.
                itemStack.setTag(null);
            }
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

        @SubscribeEvent
        public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
            Entity entity = event.getEntity();
            if (entity instanceof ThorEntity thor && !thor.level.isClientSide && !thor.isSummoningComplete()) {
                thor.setNoAi(true);
                Level level = thor.level;
                ServerLevel serverLevel = ((ServerLevel) level);
                serverLevel.setWeatherParameters(0, 60000, true, true);
                thor.startPlayingBossMusic();
            }
        }

        /**
         * Brok and Sindri were once villagers with their own professions. Vanilla handed those professions out to
         * random zombie villagers and to villagers taking some job sites, so they were removed. Saves still list them:
         * this drops them silently, and villagers or zombie villagers that had one simply become unemployed.
         */
        @SubscribeEvent
        public static void onMissingMappings(MissingMappingsEvent event) {
            for (MissingMappingsEvent.Mapping<VillagerProfession> mapping : event.getMappings(ForgeRegistries.Keys.VILLAGER_PROFESSIONS, DivineRelics.MODID)) {
                if (REMOVED_PROFESSIONS.contains(mapping.getKey().getPath())) {
                    mapping.ignore();
                }
            }
        }

        private static final Set<String> REMOVED_PROFESSIONS = Set.of("brok_master", "sindri_master");

        @SubscribeEvent
        public static void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
            Player player = event.getEntity();
            Level level = player.getLevel();

            if (!level.isClientSide) {
                setLoggedOutCap(player);
            }
        }

        private static void setLoggedOutCap(Player player) {
            player.getCapability(FightsThorProvider.FIGHTS_THOR).ifPresent(fightsThorCap -> {
                if (!fightsThorCap.getThorList().isEmpty()) {
                    fightsThorCap.setHasLoggedOut(true);
                }
            });
        }

        public static boolean isValidBindingItem(Item item) {
            return item instanceof Mjolnir || item instanceof DraupnirSpear || item instanceof LeviathanAxe;
        }

        public static void bindItemToEntity(Entity entity, ItemStack itemStack) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            nbt.putString("OwnerUUID", entity.getUUID().toString());
            nbt.putString("OwnerNickname", entity.getDisplayName().getString());
        }

        public static String getOwner(ItemStack itemStack) {
            // getTag(), not getOrCreateTag(): this is called for every item picked up by any player, and creating an
            // empty tag made every picked-up stack incompatible (non-stackable) with the same untagged item.
            CompoundTag nbt = itemStack == null ? null : itemStack.getTag();
            return nbt == null ? "" : nbt.getString("OwnerUUID");
        }

        public static double getVelocity(Player player) {
            double deltaX = player.getX() - player.xOld;
            double deltaY = player.getY() - player.yOld;
            double deltaZ = player.getZ() - player.zOld;
            return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        }

        private static boolean isValidAttacker(Entity attacker, Entity directAttacker) {
            return !(directAttacker instanceof ThrownDraupnirSpear) && (attacker instanceof LivingEntity livingEntity && !(livingEntity.getMainHandItem().getItem() == DRItems.DRAUPNIR_SPEAR.get() && directAttacker == attacker) && !(livingEntity instanceof ThorEntity));
        }

        /** Whether the Heimdall Gauntlet's Bifrost Protection lets {@code target} dodge this attack. */
        private static boolean canBifrostDodge(LivingEntity target, @Nullable Entity attacker, @Nullable Entity directAttacker) {
            return target.hasEffect(DREffects.BIFROST_PROTECTION.get())
                    && !(target instanceof Player player && player.getAbilities().instabuild)
                    && attacker != null && isValidAttacker(attacker, directAttacker);
        }

        /** Teleports {@code target} out of the way of an attack coming along {@code attackDirection}. */
        private static void bifrostDodge(LivingEntity target, Entity attacker, Vec3 attackDirection) {
            Level level = target.getLevel();
            Vec3 targetPos = target.position();
            dodgeAttack(level, target, new Vec2((float) attackDirection.x, (float) attackDirection.z));
            if (!(attacker instanceof Player)) {
                level.playSound(null, new BlockPos(targetPos), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1f, 1f);
            }
            target.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.getEyePosition());
        }

        /**
         * Projectiles fly on past an entity that dodges them with Bifrost Protection, the way they fly past an enderman,
         * instead of bouncing off it.
         * <p>
         * Runs on both sides: the client knows its own player's effects, so that player sees the projectile fly on at
         * once. For everyone else, the server re-sends the projectile's motion.
         */
        @SubscribeEvent
        public static void onProjectileImpact(ProjectileImpactEvent event) {
            if (!(event.getRayTraceResult() instanceof EntityHitResult hit) || !(hit.getEntity() instanceof LivingEntity target)) {
                return;
            }
            Projectile projectile = event.getProjectile();
            Entity attacker = projectile.getOwner();
            if (!isDodgeableProjectile(projectile) || !canBifrostDodge(target, attacker, projectile)
                    // Attacks that are ignored anyway (see LivingAttackEvent) keep their usual outcome.
                    || projectile instanceof ThrownMjolnir && attacker == target
                    || attacker instanceof Player attackerPlayer && isDRWeapon(projectile) && hasTeammate(attackerPlayer, target)) {
                return;
            }
            if (projectile instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
                markPierced(arrow, target);
            }
            if (!target.getLevel().isClientSide) {
                Vec3 motion = projectile.getDeltaMovement();
                // Out of the projectile's path; falling straight down, away from where it was shot from.
                Vec3 direction = motion.horizontalDistanceSqr() > 1.0E-6D ? motion : target.position().subtract(attacker.position());
                bifrostDodge(target, attacker, direction);
                projectile.hasImpulse = true;
            }
            event.setCanceled(true);
        }

        /**
         * Fishing hooks and experience bottles are no attacks. Shulker bullets home in: one flying on would keep chasing
         * and forcing dodges, so it hits, and the attack itself is dodged as before.
         */
        private static boolean isDodgeableProjectile(Projectile projectile) {
            return !(projectile instanceof FishingHook || projectile instanceof ThrownExperienceBottle || projectile instanceof ShulkerBullet);
        }

        @Nullable
        private static Field piercingIgnoreEntityIds;
        private static boolean piercingFieldLookedUp;

        /**
         * A piercing arrow retries every entity along its path within the same tick until one is marked as pierced, which
         * vanilla only does on an actual hit: a dodged one would be retried forever. It counts as pierced now.
         */
        private static void markPierced(AbstractArrow arrow, Entity target) {
            try {
                if (!piercingFieldLookedUp) {
                    piercingFieldLookedUp = true;
                    piercingIgnoreEntityIds = ObfuscationReflectionHelper.findField(AbstractArrow.class, "f_36701_");
                }
                if (piercingIgnoreEntityIds != null) {
                    IntOpenHashSet pierced = (IntOpenHashSet) piercingIgnoreEntityIds.get(arrow);
                    if (pierced == null) {
                        pierced = new IntOpenHashSet(5);
                        piercingIgnoreEntityIds.set(arrow, pierced);
                    }
                    pierced.add(target.getId());
                    return;
                }
            } catch (ReflectiveOperationException | RuntimeException ignored) {
                piercingIgnoreEntityIds = null;
            }
            arrow.setPierceLevel((byte) 0); // no access to the set: it simply stops piercing
        }

        public static boolean isDRWeapon(Entity directAttacker) {
            return directAttacker instanceof ThrownMjolnir || directAttacker instanceof ThrownDraupnirSpear || directAttacker instanceof ThrownLeviathanAxe;
        }

        public static void dodgeAttack(Level level, LivingEntity hurtEntity, Vec2 attackVector) {
            Vec3 entityPos = hurtEntity.position();

            double entityX = hurtEntity.getX();
            double entityY = hurtEntity.getY();
            double entityZ = hurtEntity.getZ();

            if (hurtEntity.isPassenger()) { hurtEntity.stopRiding(); }

            Vec2 attackVNorm = findNormVec(attackVector);

            double scale = hurtEntity.getRandom().nextFloat() * 1.5 + 0.5;

            Vec3 tpVector = getTPVector(level, attackVNorm, entityPos, scale);

            hurtEntity.teleportTo(tpVector.x, tpVector.y, tpVector.z);

//            if (level instanceof ServerLevel) {
//                GauntletParticleS2CPacket packet = new GauntletParticleS2CPacket(entityX, entityY, entityZ, tpVector.x, tpVector.z);
//                DRMessages.sendToChunk(packet, level.getChunkAt(hurtEntity.blockPosition()));
//            }
        }
    }
}
