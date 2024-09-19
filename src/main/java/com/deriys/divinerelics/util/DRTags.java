package com.deriys.divinerelics.util;

import com.deriys.divinerelics.DivineRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DRTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_SVARTALFHEIM_STEEL_TOOL
                = tag("needs_svartalfheim_steel_tool");

        public static final TagKey<Block> NEEDS_ASGARDIAN_STEEL_TOOL
                = tag("needs_asgardian_steel_tool");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(DivineRelics.MODID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }
}
