package com.deriys.divinerelics.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Collections;

public class DwarvenCompass extends Item {
    public DwarvenCompass(Properties p_40718_) {
        super(p_40718_);
    }

    public void setShopX(ItemStack stack, int x) {
        stack.getOrCreateTag().putInt("nearestShopX", x);
    }

    public void setShopY(ItemStack stack, int y) {
        stack.getOrCreateTag().putInt("nearestShopY", y);
    }

    public int getShopX(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nearestShopX");
    }

    public int getShopY(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nearestShopY");
    }

    public void setShopZ(ItemStack stack, int z) {
        stack.getOrCreateTag().putInt("nearestShopZ", z);
    }

    public int getShopZ(ItemStack stack) {
        return stack.getOrCreateTag().getInt("nearestShopZ");
    }

    @Override
    public boolean isFoil(ItemStack p_40739_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!level.isClientSide) {
            boolean containsPos = stack.getOrCreateTag().contains("nearestShopX");
            if (!containsPos) {
                BlockPos shopPos = findNearestShop(((ServerLevel) level), entity.getOnPos());
                if (shopPos != null) {
                    setShopX(stack, shopPos.getX());
                    setShopY(stack, shopPos.getX());
                    setShopZ(stack, shopPos.getZ());
                }
            }
        }
    }

    public static BlockPos findNearestShop(ServerLevel level, BlockPos pos) {
        ResourceKey<Structure> structureKey = ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation("divinerelics:brok_and_sindri_shop"));
        Holder<Structure> structureHolder = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).getHolderOrThrow(structureKey);

        HolderSet<Structure> holderset = HolderSet.direct(Collections.singletonList(structureHolder));
        Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator().findNearestMapStructure(level, holderset, pos, 100, false);
        if (result != null) {
            return result.getFirst();
        }
        return null;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            BlockPos playerOnPos = player.getOnPos();
            BlockPos shopPos = findNearestShop(((ServerLevel) level), playerOnPos);
            if (shopPos != null) {
                ItemStack stack = player.getItemInHand(hand);
                player.sendSystemMessage(Component.literal("The nearest Brok and Sindri Shop is " + Math.round(Math.sqrt(playerOnPos.distSqr(new BlockPos(shopPos.getX(), playerOnPos.getY(), shopPos.getZ())))) + " blocks away."));
                setShopX(stack, shopPos.getX());
                setShopY(stack, shopPos.getX());
                setShopZ(stack, shopPos.getZ());
            }
        }
        return super.use(level, player, hand);
    }
}
