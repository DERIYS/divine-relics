package com.deriys.divinerelics.entities.entity;

import com.deriys.divinerelics.init.DRBlocks;
import com.deriys.divinerelics.init.DRItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class SindriEntity extends DwarfMerchantEntity {
    private static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = new Int2ObjectOpenHashMap<>();

    static {
        TRADES.put(1, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.COMPRESSED_HACKSILVER.get(), 8),
                        new ItemStack(DRItems.HACKSILVER_INGOT.get(), 9), 5, 2, 0.0F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 2),
                        new ItemStack(DRItems.HACKSILVER_INGOT.get(), 16), 2, 2, 0.0F)});
        TRADES.put(2, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 1),
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 2), 5, 9, 0.02F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.RAW_SVARTALFHEIM_STEEL.get(), 4),
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 5), 4, 9, 0.02F)});
        TRADES.put(3, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 1),
                        new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 6), 4, 15, 0.02F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2),
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 6), 3, 18, 0.02F)});
        TRADES.put(4, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 4),
                        new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 16),
                        new ItemStack(DRItems.HEIMDALL_GAUNTLET.get(), 1), 1, 120, 30F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                        new ItemStack(Blocks.BIRCH_LOG, 1),
                        new ItemStack(DRItems.YGGDRASIL_BRANCH.get(), 1), 1, 120, 30F)});
        TRADES.put(5, new VillagerTrades.ItemListing[]{
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 1),
                        new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2),
                        new ItemStack(DRItems.DRAUPNIR_RING.get(), 1), 1, 200, 40F),
                (trader, rand) -> new MerchantOffer(
                        new ItemStack(DRItems.DRAUPNIR_SPEAR_BASE.get(), 1),
                        new ItemStack(DRItems.DRAUPNIR_RING.get(), 1),
                        new ItemStack(DRItems.DRAUPNIR_SPEAR.get(), 1), 1, 200, 40F)});
    }

    public SindriEntity(EntityType<? extends SindriEntity> entityType, Level level) {
        super(entityType, level, "animation.sindri.walk", "animation.sindri.idle");
    }

    @Override
    protected Int2ObjectMap<VillagerTrades.ItemListing[]> getTradesByLevel() {
        return TRADES;
    }
}
