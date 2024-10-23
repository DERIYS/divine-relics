package com.deriys.divinerelics.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class DRKeyBindings {
    public static final String KEY_CATEGORY_MAIN = "key.category.divinerelics.main";
    public static final String KEY_USE_GUARDIAN_SHIELD = "key.category.divinerelics.use_guardian_shield";

    public static final KeyMapping GUARDIAN_SHIELD_KEY = new KeyMapping(KEY_USE_GUARDIAN_SHIELD, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_CAPS_LOCK, KEY_CATEGORY_MAIN);
}
