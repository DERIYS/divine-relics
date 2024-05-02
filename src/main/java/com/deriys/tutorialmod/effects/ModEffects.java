package com.deriys.tutorialmod.effects;

import com.deriys.tutorialmod.TutorialMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TutorialMod.MODID);

    public static RegistryObject<MobEffect> BIFROST_PROTECTION = MOB_EFFECTS.register("bifrost_protection",
            () -> new BifrostProtection(MobEffectCategory.BENEFICIAL, 3124687));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
