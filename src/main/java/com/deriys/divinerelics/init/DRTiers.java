package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.util.DRTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class DRTiers {
    public static final Tier SVARTALFHEIM_STEEL_TIER = TierSortingRegistry.registerTier(
            new ForgeTier(4, 200, 9f, 4f, 15,
                    DRTags.Blocks.NEEDS_SVARTALFHEIM_STEEL_TOOL, () -> Ingredient.of(DRItems.SVARTALFHEIM_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "svartalfheim_steel"), List.of(Tiers.NETHERITE), List.of());
    public static final Tier DRAUPNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(DRItems.ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "draupnir"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier MJOLNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(DRItems.ASGARDIAN_STEEL_INGOT.get())),
            new ResourceLocation(DivineRelics.MODID, "mjolnir"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier LEVIATHAN = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(DivineRelics.MODID, "leviathan"), List.of(Tiers.NETHERITE), List.of());
}
