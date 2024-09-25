package com.deriys.divinerelics.items;

import com.deriys.divinerelics.entities.entity.ThorEntity;
import com.deriys.divinerelics.init.DREntitiyTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class DivineMead extends Item {
    public DivineMead(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (!level.isClientSide && player != null && player.getMainHandItem() == stack && context.getClickedFace() == Direction.UP && level.getBlockState(context.getClickedPos()).getBlock() != Blocks.LAVA) {
            ThorEntity thor = new ThorEntity(DREntitiyTypes.THOR.get(), level);
            thor.setPos(Vec3.atCenterOf(context.getClickedPos()).add(0D, 1D, 0D));
            thor.setSummoningComplete(false);
            level.addFreshEntity(thor);
            if (!player.isCreative()) {
                player.getInventory().removeItem(stack);
            }
        }
        return InteractionResult.CONSUME;
    }
}
