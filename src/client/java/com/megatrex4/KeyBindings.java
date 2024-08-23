package com.megatrex4;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyBinding randomPlaceKey;

    public static void registerKeyBindings() {
        randomPlaceKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.randomblockplacement.toggle", // Updated to match usage
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.randomblockplacement"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (randomPlaceKey.wasPressed()) {
                RandomBlockPlacementClient.onRandomPlaceKeyPressed();
            }
        });
    }
}
