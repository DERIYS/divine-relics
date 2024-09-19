package com.deriys.divinerelics.event;

import com.deriys.divinerelics.DivineRelics;
import com.deriys.divinerelics.capabilities.stuck_spears.StuckSpearsProvider;
import com.deriys.divinerelics.core.networking.DRMessages;
import com.deriys.divinerelics.core.networking.packets.LeviathanBindingC2SPacket;
import com.deriys.divinerelics.core.networking.packets.MjolnirBindingC2SPacket;
import com.deriys.divinerelics.core.networking.packets.StuckSpearsS2CPacket;
import com.deriys.divinerelics.dwarfs.DRDwarfs;
import com.deriys.divinerelics.init.DRBlocks;
import com.deriys.divinerelics.init.DRItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = DivineRelics.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DRForgeEventBusEvents {

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket mjolnirPacket = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(mjolnirPacket);
            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().isEmpty()) {
            MjolnirBindingC2SPacket packet = new MjolnirBindingC2SPacket();
            DRMessages.sendToServer(packet);

            LeviathanBindingC2SPacket leviathanPacket = new LeviathanBindingC2SPacket();
            DRMessages.sendToServer(leviathanPacket);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getTarget();
            entity.getCapability(StuckSpearsProvider.STUCK_SPEARS).ifPresent(cap -> DRMessages.sendToPlayer(new StuckSpearsS2CPacket(entity.getId(), cap.getSpears()), (ServerPlayer) event.getEntity()));
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if(event.getType() == DRDwarfs.BROK.get()) {
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.HACKSILVER.get(), 9),
                    new ItemStack(DRItems.COMPRESSED_HACKSILVER.get(), 4),10,3,0.01F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.HACKSILVER_INGOT.get(), 16),
                    new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 2),10,5,0.01F));

            villagerLevel = 2;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.HACKSILVER_INGOT.get(), 10),
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 2),4,10,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 18),
                    new ItemStack(DRItems.RAW_SVARTALFHEIM_STEEL.get(), 3),4,15,0.02F));

            villagerLevel = 3;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 18),
                    new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 3),4, 25,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 2),
                    new ItemStack(DRItems.HACKSILVER_INGOT.get(), 32),
                    new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2),2,30,0.02F));

            villagerLevel = 4;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                    new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 4),
                    new ItemStack(DRItems.MOTOSIGNIR.get(), 1),1,120,0.5F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 3),
                    new ItemStack(Items.SHIELD, 1),
                    new ItemStack(DRItems.GUARDIAN_SHIELD.get(), 1),1,120,0.5F));

            villagerLevel = 5;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                    new ItemStack(DRItems.FROZEN_FLAME.get(), 1),1,120,0.8F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.MJOLNIR_HANDLE.get(), 1),
                    new ItemStack(DRItems.MJOLNIR_HEAD.get(), 1),
                    new ItemStack(DRItems.MJOLNIR.get(), 1),1,120,0.8F));


        } else if (event.getType() == DRDwarfs.SINDRI.get()) {

            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.COMPRESSED_HACKSILVER.get(), 8),
                    new ItemStack(DRItems.HACKSILVER_INGOT.get(), 9),5,3,0.01F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 2),
                    new ItemStack(DRItems.HACKSILVER_INGOT.get(), 16),10,2,0.01F));

            villagerLevel = 2;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRBlocks.HACKSILVER_BLOCK.get(), 1),
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_NUGGET.get(), 2),4,10,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.RAW_SVARTALFHEIM_STEEL.get(), 4),
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 5),4,17,0.02F));

            villagerLevel = 3;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.RAW_ASGARDIAN_STEEL.get(), 1),
                    new ItemStack(DRItems.ASGARDIAN_STEEL_NUGGET.get(), 6),4,20,0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2),
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 6), 3,25,0.02F));

            villagerLevel = 4;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 4),
                    new ItemStack(DRItems.SVARTALFHEIM_STEEL_INGOT.get(), 16),
                    new ItemStack(DRItems.HEIMDALL_GAUNTLET.get(), 1),1,120,0.5F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 2),
                    new ItemStack(Blocks.BIRCH_LOG, 1),
                    new ItemStack(DRItems.YGGDRASILS_TWIG.get(), 1),1,120,0.5F));

            villagerLevel = 5;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.PERFECT_ASGARDIAN_STEEL_INGOT.get(), 1),
                    new ItemStack(DRItems.ASGARDIAN_STEEL_INGOT.get(), 2),
                    new ItemStack(DRItems.DRAUPNIR_RING.get(), 1),1,20,0.8F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(DRItems.DRAUPNIR_SPEAR_BASE.get(), 1),
                    new ItemStack(DRItems.DRAUPNIR_RING.get(), 1),
                    new ItemStack(DRItems.DRAUPNIR_SPEAR.get(), 1),1,20,0.8F));
        }
    }
}
