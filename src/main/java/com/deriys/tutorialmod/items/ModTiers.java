package com.deriys.tutorialmod.items;

import com.deriys.tutorialmod.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModTiers {
    public static final Tier MOTOSIGNIR = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(TutorialMod.MODID, "motosignir"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier HEIMDALL_GAUNTLET = TierSortingRegistry.registerTier(
            new ForgeTier(5, 0, 9f, 4f, 15,
                    Tags.Blocks.NEEDS_NETHERITE_TOOL, () -> Ingredient.of(Items.NETHERITE_INGOT)),
            new ResourceLocation(TutorialMod.MODID, "heimdall_gauntlet"), List.of(Tiers.NETHERITE), List.of());
}
