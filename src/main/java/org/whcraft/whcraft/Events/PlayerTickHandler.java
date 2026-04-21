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

package org.whcraft.whcraft.Events;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.whcraft.whcraft.Component.Astartes.AstartesStatusEffectApplicator;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftComponents;

import java.util.ArrayList;
import java.util.List;

public class PlayerTickHandler {

    /**
     * 每 tick 检测玩家植入的器官，移除应免疫的负面状态效果。<br/>
     * 应在服务端 tick 事件中对每个在线玩家调用。
     *
     * @param player 目标玩家
     */
    public static void onAstartesTick(ServerPlayerEntity player) {
        if (player.getWorld().isClient) return;

        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        // 休眠神经节：免疫挖掘疲劳和凋零
        if (implants.contains(ImplantType.CATALEPSEAN_NODE)) {
            player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            player.removeStatusEffect(StatusEffects.WITHER);
        }

        // 视眼神经叶：免疫黑暗
        if (implants.contains(ImplantType.OCCULOBE)) {
            player.removeStatusEffect(StatusEffects.DARKNESS);
        }

        // 莱曼之耳：免疫反胃
        if (implants.contains(ImplantType.LYMANS_EAR)) {
            player.removeStatusEffect(StatusEffects.NAUSEA);
        }

        // 卵石肾脏：生命值已满时，免疫所有负面状态效果
        if (implants.contains(ImplantType.OOLITIC_KIDNEY) && player.getHealth() >= player.getMaxHealth()) {
            List<StatusEffect> toRemove = new ArrayList<>();
            for (StatusEffectInstance instance : player.getStatusEffects()) {
                if (!instance.getEffectType().isBeneficial()) {
                    toRemove.add(instance.getEffectType());
                }
            }
            for (StatusEffect effect : toRemove) {
                player.removeStatusEffect(effect);
            }
        }

        // 神经喉头：生命值与饥饿值均已满时，获得5秒力量III和5秒急迫III
        if (implants.contains(ImplantType.NEUROGLOTTIS)) {
            if (player.getHealth() >= player.getMaxHealth() && player.getHungerManager().getFoodLevel() >= 20) {
                // 力量 III (amplifier = 2)
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.STRENGTH, 100, 2, // 5秒 = 100 ticks
                        false, false, true
                ));
                // 急迫 III (amplifier = 2)
                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.HASTE, 100, 2,
                        false, false, true
                ));
            }
        }

        // 实现器官添加状态效果
        AstartesStatusEffectApplicator.updateEffects(player);
    }
}