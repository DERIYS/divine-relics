package com.deriys.divinerelics.init;

import com.deriys.divinerelics.DivineRelics;
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
    public static final Tier MOTOSIGNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(DivineRelics.MODID, "motosignir"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier HEIMDALL_GAUNTLET = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(DivineRelics.MODID, "heimdall_gauntlet"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier MJOLNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(DivineRelics.MODID, "mjolnir"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier LEVIATHAN = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(DivineRelics.MODID, "leviathan"), List.of(Tiers.NETHERITE), List.of());
}
