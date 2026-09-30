package com.deriys.divinerelics.entities.entity;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.BlockEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * What a thrown weapon (Mjölnir, Leviathan Axe) does while flying back to its owner. It has no physics then, so it
 * passes through everything and AbstractArrow detects no hits: these break the blocks it flies into and hit the
 * entities on its path instead.
 */
final class ReturnFlight {
    private ReturnFlight() {
    }

    /**
     * Breaks every block the weapon flies straight into between {@code from} and {@code to}. Blocks it may not break
     * (unbreakable ones, chests, shulker boxes, protected areas) are left untouched and simply flown through.
     */
    static void smashBlocks(Entity weapon, Player owner, Vec3 from, Vec3 to) {
        if (!owner.mayBuild()) {
            return;
        }
        Level level = weapon.level;
        boolean dropItems = !owner.getAbilities().instabuild;
        BlockGetter.traverseBlocks(from, to, null, (context, cursor) -> {
            BlockPos pos = cursor.immutable();
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && state.getCollisionShape(level, pos).clip(from, to, pos) != null
                    && canSmash(level, owner, pos, state)) {
                level.destroyBlock(pos, dropItems, weapon);
            }
            return null;
        }, context -> null);
    }

    private static boolean canSmash(Level level, Player owner, BlockPos pos, BlockState state) {
        if (state.getDestroySpeed(level, pos) < 0.0F || isChestOrShulkerBox(state)) {
            return false;
        }
        // Lets spawn protection and claim/protection mods veto it, as if the owner had broken the block.
        return level.mayInteract(owner, pos)
                && !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, owner));
    }

    private static boolean isChestOrShulkerBox(BlockState state) {
        return state.is(Tags.Blocks.CHESTS) || state.is(BlockTags.SHULKER_BOXES)
                || state.getBlock() instanceof AbstractChestBlock || state.getBlock() instanceof ShulkerBoxBlock;
    }

    /**
     * Hits every entity between {@code from} and {@code to} that {@code canHit} accepts, except the owner and the ones
     * in {@code alreadyHit} (hit earlier on this return), which the newly hit ones are added to.
     */
    static void hitEntities(Projectile weapon, Player owner, Vec3 from, Vec3 to, IntSet alreadyHit,
                            Predicate<Entity> canHit, Consumer<EntityHitResult> onHit) {
        for (Entity target : weapon.level.getEntities(weapon, new AABB(from, to).inflate(1.0D), canHit)) {
            if (target == owner || owner.isPassengerOfSameVehicle(target) || alreadyHit.contains(target.getId())) {
                continue;
            }
            AABB hitBox = target.getBoundingBox().inflate(0.3D); // same leeway as vanilla projectile hit detection
            if (!hitBox.contains(from) && hitBox.clip(from, to).isEmpty()) {
                continue;
            }
            if (target instanceof Player victim && !owner.canHarmPlayer(victim)) {
                continue;
            }
            alreadyHit.add(target.getId());
            EntityHitResult hitResult = new EntityHitResult(target);
            if (!ForgeEventFactory.onProjectileImpact(weapon, hitResult)) {
                onHit.accept(hitResult);
            }
        }
    }
}
