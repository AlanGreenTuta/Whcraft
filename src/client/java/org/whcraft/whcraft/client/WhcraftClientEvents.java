package org.whcraft.whcraft.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import org.whcraft.whcraft.WhcraftNetworking;

public class WhcraftClientEvents {
    public static void initialize() {
        // 检测按键
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (WhcraftClientKeyBindings.detonateServoSkull.wasPressed()) {
                if (client.player != null) {
                    ClientPlayNetworking.send(WhcraftNetworking.DETONATE_SERVO_SKULL, PacketByteBufs.empty());
                }
            }
        });
    }
}