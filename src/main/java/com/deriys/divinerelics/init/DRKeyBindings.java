package com.deriys.divinerelics.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class DRKeyBindings {
    public static final String KEY_CATEGORY_MAIN = "key.category.divinerelics.main";
    // Keep the saved key ID so existing player remappings survive the broader shield support.
    public static final String KEY_USE_OFFHAND_SHIELD = "key.category.divinerelics.use_guardian_shield";

    public static final KeyMapping OFFHAND_SHIELD_KEY = new KeyMapping(KEY_USE_OFFHAND_SHIELD, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, KEY_CATEGORY_MAIN);
}
