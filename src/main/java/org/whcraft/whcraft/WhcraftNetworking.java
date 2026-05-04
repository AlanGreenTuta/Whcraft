package org.whcraft.whcraft;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

import java.util.Comparator;
import java.util.List;

public class WhcraftNetworking {
    public static final Identifier DETONATE_SERVO_SKULL = new Identifier("whcraft", "detonate_servo_skull");

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(DETONATE_SERVO_SKULL, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                Box searchBox = new Box(player.getBlockPos()).expand(128.0);
                List<ServoSkullEntity> skulls = player.getWorld().getEntitiesByClass(
                        ServoSkullEntity.class,
                        searchBox,
                        skull -> player.getUuid().equals(skull.getOwnerUuid()) && skull.getRetrofits().contains("weaponry")
                );

                if (skulls.isEmpty()) return;
                ServoSkullEntity target = skulls.stream()
                        .min(Comparator.comparingDouble(s -> s.squaredDistanceTo(player)))
                        .orElse(null);

                if (target != null) {
                    target.detonate();
                }
            });
        });
    }
}