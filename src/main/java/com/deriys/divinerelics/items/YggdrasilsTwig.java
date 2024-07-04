package com.deriys.divinerelics.items;

import com.deriys.divinerelics.capabilities.teammates.TeammatesProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.deriys.divinerelics.capabilities.teammates.TeammatesProvider.hasTeammate;

public class YggdrasilsTwig extends Item {
    public YggdrasilsTwig(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand hand) {
        if (livingEntity instanceof LivingEntity && !player.getLevel().isClientSide) {
            player.getCapability(TeammatesProvider.TEAMMATES).ifPresent(teammates -> {
                if (hasTeammate(player, livingEntity)) {
                    player.sendSystemMessage(Component.literal("Removed " + livingEntity.getDisplayName().getString() + " from your team!"));
                    teammates.removeTeammate(livingEntity.getUUID().toString());
                } else {
                    player.sendSystemMessage(Component.literal("Added " + livingEntity.getDisplayName().getString() + " to your team!"));
                    teammates.addTeammate(livingEntity.getUUID().toString());
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.literal("A fragment from the cosmic tree itself that forges unseen bonds of unity among allies as well as severs them."));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }
    }
}
