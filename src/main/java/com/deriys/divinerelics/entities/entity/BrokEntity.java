package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.init.DRBlocks;
import com.deriys.divinerelics.init.DRItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

public class BrokEntity extends DwarfMerchantEntity {
    private static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = new Int2ObjectOpenHashMap<>();

    static {
        TRADES.put(1, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.HACKSILVER.get(), 9),
                        new ItemStack(DRItems.COMPRESSED_HACKSILVER.get(), 4), 5, 2, 0.0F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.HACKSILVER_INGOT.get(), 16),
                        new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 2), 2, 2, 0.0F)});
        TRADES.put(2, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.HACKSILVER_INGOT.get(), 8),
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 2), 4, 9, 0.02F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 18),
                        new ItemStack(DRItems.RAW_SVARTALFHEIM_STEEL.get(), 3), 4, 9, 0.02F)});
        TRADES.put(3, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 18),
                        new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 3), 4, 14, 0.02F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 2),
                        new ItemStack(DRItems.HACKSILVER_INGOT.get(), 32),
                        new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2), 2, 16, 0.02F)});
        TRADES.put(4, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                        new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 4),
                        new ItemStack(DRItems.MOTOSIGNIR.get(), 1), 1, 120, 30F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 3),
                        new ItemStack(Items.SHIELD, 1),
                        new ItemStack(DRItems.GUARDIAN_SHIELD.get(), 1), 1, 120, 30F)});
        TRADES.put(5, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                        new ItemStack(DRItems.FROZEN_FLAME.get(), 1), 1, 120, 40F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.MJOLNIR_HANDLE.get(), 1),
                        new ItemStack(DRItems.MJOLNIR_HEAD.get(), 1),
                        new ItemStack(DRItems.MJOLNIR.get(), 1), 1, 120, 40F)});
    }

    public BrokEntity(EntityType<? extends BrokEntity> entityType, Level level) {
        super(entityType, level, "animation.brok.walk", "animation.brok.idle");
    }

    @Override
    protected Int2ObjectMap<VillagerTrades.ItemListing[]> getTradesByLevel() {
        return TRADES;
    }
}
