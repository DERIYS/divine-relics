package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.util.DRTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class DRTiers {
    public static final Tier ASGARDIAN_STEEL_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(4, 3000, 9.0f, 6f, 20,
                    DRTags.Blocks.NEEDS_ASGARDIAN_STEEL_TOOL, () -> Ingredient.of(DRItems.ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "asgardian_steel"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier SVARTALFHEIM_STEEL_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(5, 2200, 10.0f, 4f, 15,
                    DRTags.Blocks.NEEDS_SVARTALFHEIM_STEEL_TOOL, () -> Ingredient.of(DRItems.SVARTALFHEIM_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "svartalfheim_steel"), List.of(DRTiers.ASGARDIAN_STEEL_TIER), List.of());
    public static final Tier DRAUPNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 5f, 35,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "draupnir"), List.of(DRTiers.SVARTALFHEIM_STEEL_TIER), List.of());

    public static final Tier MJOLNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 35,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "mjolnir"), List.of(DRTiers.SVARTALFHEIM_STEEL_TIER), List.of());

    public static final Tier LEVIATHAN = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 35,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "leviathan"), List.of(DRTiers.SVARTALFHEIM_STEEL_TIER), List.of());
}
