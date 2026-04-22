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

package org.whcraft.whcraft.Component.Astartes;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.whcraft.whcraft.WhcraftComponents;
import org.whcraft.whcraft.WhcraftGameRules;

/**
 * <p>处理持续型阿斯塔特器官状态效果的应用。</p>
 * 本类每 tick 为拥有器官的玩家添加持续 2 秒的效果，<br/>
 * 不主动移除任何效果。当器官消失后，效果在 2 秒内自然消失，不影响其他来源的同名效果。
 */
public class AstartesStatusEffectApplicator {

    /**
     * 根据玩家当前植入的器官，添加对应的状态效果。<br/>
     * 应在每 tick 调用（仅服务端）。
     *
     * @param player 目标玩家
     */
    public static void updateEffects(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        // 强肌腺体：力量 II，跳跃提升 III
        if (implants.contains(ImplantType.BISCOPEA)) {
            applyEffect(player, StatusEffects.STRENGTH, 1);      // 力量 II
            applyEffect(player, StatusEffects.JUMP_BOOST, 2);    // 跳跃提升 III
        }

        // 拉瑞曼脏器：生命恢复 III
        if (implants.contains(ImplantType.LARRAMANS_ORGAN)) {
            applyEffect(player, StatusEffects.REGENERATION, 2);  // 生命恢复 III
        }

        // 多肺：水下呼吸 I，迅捷 II
        if (implants.contains(ImplantType.MULTI_LUNG)) {
            applyEffect(player, StatusEffects.WATER_BREATHING, 0);
            applyEffect(player, StatusEffects.SPEED, 1);
        }

        // 视眼神经叶：夜视 I
        if (implants.contains(ImplantType.OCCULOBE)) {
            applyEffect(player, StatusEffects.NIGHT_VISION, 0);
        }
    }

    /**
     * 为玩家添加一个短时状态效果。
     * 仅当玩家当前没有该效果，或剩余时间不足 10 ticks 时才添加，避免每 tick 重复刷新。
     *
     * @param player    目标玩家
     * @param effect    状态效果类型
     * @param amplifier 效果等级（0 = I 级）
     */
    private static void applyEffect(PlayerEntity player, net.minecraft.entity.effect.StatusEffect effect, int amplifier) {
        StatusEffectInstance existing = player.getStatusEffect(effect);

        boolean isNightVision = effect == StatusEffects.NIGHT_VISION;
        if (isNightVision && (existing == null || existing.getDuration() != -1)) {
            // 夜视在刷新时会出现屏幕闪烁，所以改为获得永久效果
            boolean showIcon = player.getWorld().getGameRules().getBoolean(WhcraftGameRules.SHOW_ASTARTES_STATUS_EFFECT_ICON);
            player.addStatusEffect(new StatusEffectInstance(
                    effect,
                    -1,
                    amplifier,
                    false, // 无环境来源
                    false, // 不显示粒子（可根据需要调整）
                    showIcon // 根据游戏规则决定是否显示图标
            ));
        }

        int threshold = player.getWorld().getGameRules().getInt(WhcraftGameRules.ASTARTES_STATUS_EFFECT_THRESHOLD);
        if ((existing == null || existing.getDuration() < threshold) && (! isNightVision)) {
            int duration = player.getWorld().getGameRules().getInt(WhcraftGameRules.ASTARTES_STATUS_EFFECT_DURATION);
            // 从游戏规则获取是否显示图标
            boolean showIcon = player.getWorld().getGameRules().getBoolean(WhcraftGameRules.SHOW_ASTARTES_STATUS_EFFECT_ICON);
            player.addStatusEffect(new StatusEffectInstance(
                    effect,
                    duration,
                    amplifier,
                    false, // 无环境来源
                    false, // 不显示粒子（可根据需要调整）
                    showIcon // 根据游戏规则决定是否显示图标
            ));
        }
    }

    /**
     * 移除玩家身上的夜视效果。<br/>
     * 当视眼神经叶器官被移除时调用。
     *
     * @param player 目标玩家
     */
    public static void removeNightVision(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        player.removeStatusEffect(StatusEffects.NIGHT_VISION);
    }
}