package org.whcraft.whcraft.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class WhcraftClientKeyBindings {
    public static KeyBinding detonateServoSkull;

    public static void initialize() {
        detonateServoSkull = new KeyBinding(
                "key.whcraft.detonate_servo_skull",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "key.whcraft.whcraft"
        );
        // 注册键绑定
        KeyBindingHelper.registerKeyBinding(detonateServoSkull);
    }
}