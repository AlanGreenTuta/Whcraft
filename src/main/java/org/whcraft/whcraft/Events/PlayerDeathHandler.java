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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.whcraft.whcraft.Component.Astartes.AstartesOrganImplants;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.Component.Astartes.PlayerImplantsComponent;
import org.whcraft.whcraft.Items.GeneSeed.GeneSeed;
import org.whcraft.whcraft.WhcraftComponents;
import org.whcraft.whcraft.WhcraftGameRules;

import java.util.List;
import java.util.Set;

public class PlayerDeathHandler {

    /**
     * 实体死亡后触发，若是阿斯塔特，移除所有改造。
     * @param entity 死亡实体
     * @param source 伤害来源
     */
    public static void onAstartesDeath(LivingEntity entity, DamageSource source) {
        if (entity instanceof ServerPlayerEntity player) {
            boolean keepImplants = player.getWorld().getGameRules().getBoolean(WhcraftGameRules.KEEP_ASTARTES_IMPLANT);
            AstartesOrganImplants component = WhcraftComponents.IMPLANT.get(player);
            if (!(component instanceof PlayerImplantsComponent impl)) return;

            // 检查是否掉落基因种子
            if (!keepImplants && impl.hasImplant(ImplantType.PROGENOID_GLANDS)) {
                Set<ImplantType> currentImplants = impl.getImplants();
                if (!currentImplants.isEmpty()) {
                    ItemStack seed = GeneSeed.createWithImplants(currentImplants);
                    player.dropStack(seed, player.getStandingEyeHeight());
                }
            }

            if (!keepImplants) {
                impl.removeAllImplants();
            }
        }
    }

    /**
     * 在实体即将死亡时触发（ALLOW_DEATH），处理维生脑膜与皮织腺体的免死效果。
     *
     * @param entity       将死的实体
     * @param source       伤害来源
     * @param damageAmount 即将造成的伤害量
     * @return true:允许死亡 or false:阻止本次死亡
     */
    public static boolean whenAstartesDeath(LivingEntity entity, DamageSource source, float damageAmount) {
        // 仅处理玩家
        if (!(entity instanceof ServerPlayerEntity player)) {
            return true;
        }

        // 获取组件
        AstartesOrganImplants component = WhcraftComponents.IMPLANT.get(player);
        if (!(component instanceof PlayerImplantsComponent impl)) {
            return true;
        }

        // 检查是否拥有维生脑膜
        if (!impl.hasImplant(ImplantType.SUS_AN_MEMBRANE)) {
            return true; // 无该器官，正常死亡
        }

        // 检查冷却（返回 -1 表示无冷却或未植入，返回 >0 表示剩余 tick）
        int cooldown = impl.getCooldown(ImplantType.SUS_AN_MEMBRANE);
        if (cooldown > 0) {
            return true; // 冷却中，正常死亡
        }

        // 维生脑膜触发：阻止死亡，设置生命值为20
        player.setHealth(20.0f);

        // 添加状态效果：45秒生命恢复 II（等级 1）、5秒伤害吸收 II（等级 1）
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.REGENERATION, 45 * 20, 1, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.ABSORPTION, 5 * 20, 1, false, false, true));

        // 若同时拥有皮织腺体 (MUCRANOID)，附加额外效果
        if (impl.hasImplant(ImplantType.MUCRANOID)) {
            // 移除所有负面状态效果
            List<net.minecraft.entity.effect.StatusEffectInstance> effectsToRemove =
                    player.getStatusEffects().stream()
                            .filter(instance -> !instance.getEffectType().isBeneficial())
                            .toList();
            effectsToRemove.forEach(instance -> player.removeStatusEffect(instance.getEffectType()));

            // 提供 30 秒抗火 I（等级 0）
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.FIRE_RESISTANCE, 30 * 20, 0, false, false, true));
            // 提供 20 秒抗性提升 IV（等级 3）
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.RESISTANCE, 20 * 20, 3, false, false, true));
        }


        // 获取游戏规则定义的冷却值（单位：tick）
        int cooldownTicks = player.getWorld().getGameRules().getInt(WhcraftGameRules.SUSAN_MEMBRANE_COOLDOWN);

        // 设置冷却
        impl.setCooldown(ImplantType.SUS_AN_MEMBRANE, cooldownTicks);

        // 返回 false 取消本次死亡
        return false;
    }
}
