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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.whcraft.whcraft.WhcraftComponents;

public class AstartesEatHandler {

    /**
     * 在玩家食用物品后调用，处理基因侦测神经和预置胃的效果。<br/>
     * 应在服务端调用。
     *
     * @param player 进食的玩家
     * @param stack  被食用的物品栈
     */
    public static void onAstartesEat(PlayerEntity player, ItemStack stack) {
        World world = player.getWorld();
        if (world.isClient) return; // 仅服务端处理

        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        // 基因侦测神经
        if (implants.contains(ImplantType.OMOPHAGEA)) {
            var foodComponent = stack.getItem().getFoodComponent();
            if (foodComponent != null) {
                int hungerRestored = foodComponent.getHunger();
                // 恢复生命值 = 饥饿值恢复量 * 0.5，向下取整
                int healAmount = hungerRestored / 2;
                if (healAmount > 0) {
                    player.heal(healAmount);
                }
            }
        }

        // 预置胃
        if (implants.contains(ImplantType.PREOMNOR)) {
            // 遍历当前所有状态效果，移除非良性的（即负面效果）
            var effectsToRemove = player.getStatusEffects().stream()
                    .map(StatusEffectInstance::getEffectType)
                    .filter(effect -> !effect.isBeneficial())
                    .toList();
            effectsToRemove.forEach(player::removeStatusEffect);
        }
    }
}