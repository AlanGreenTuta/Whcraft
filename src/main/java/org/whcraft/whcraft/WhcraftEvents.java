/**
 * WhCraft - A Minecraft Mod about Warhammer 40,000.
 * Copyright (C) 2026 ShuShu, 42
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.whcraft.whcraft;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.whcraft.whcraft.Component.Astartes.AstartesAttributeModification;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.Events.PlayerAttackHandler;
import org.whcraft.whcraft.Events.PlayerDeathHandler;
import org.whcraft.whcraft.Events.PlayerTickHandler;

import java.util.UUID;

public class WhcraftEvents {
    public static void initialize() {

        // 应用阿斯塔特死亡时失去所有改造
        ServerLivingEntityEvents.AFTER_DEATH.register(PlayerDeathHandler::onAstartesDeath);

        // 应用阿斯塔特器官的属性修改和状态效果添加
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            AstartesAttributeModification.updateAttributes(handler.player);
        });

        // 应用阿斯塔特器官的属性修改和状态效果添加
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            AstartesAttributeModification.updateAttributes(newPlayer);
        });

        // 每 tick 处理免疫效果（休眠神经节、视眼神经叶、莱曼之耳、卵石肾脏）
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                PlayerTickHandler.onAstartesTick(player);
            }
        });

        // 应用阿斯塔特攻击效果
        AttackEntityCallback.EVENT.register(PlayerAttackHandler::onAstartesAttack);

        // 处理阿斯塔特死亡效果
        ServerLivingEntityEvents.ALLOW_DEATH.register(PlayerDeathHandler::whenAstartesDeath);

        // 处理伺服头骨主人上线
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            UUID ownerUuid = player.getUuid();

            for (ServerWorld world : server.getWorlds()) {
                for (ServoSkullEntity skull : world.getEntitiesByType(
                        WhcraftEntities.SERVO_SKULL,
                        skull -> ownerUuid.equals(skull.getOwnerUuid())
                )) {
                    skull.onOwnerReconnect(player);
                }
            }
        });
    }
}
