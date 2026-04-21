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

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftComponents;

public class PlayerAttackHandler {

    /**
     * 处理阿斯塔特玩家近战攻击时的效果<br/>
     * 在 {@link net.fabricmc.fabric.api.event.player.AttackEntityCallback} 中调用。
     *
     * @param player    攻击者玩家
     * @param world     当前世界
     * @param hand      使用的手
     * @param entity    被攻击实体
     * @param hitResult 命中结果（可能为 null）
     * @return ActionResult.PASS 以保持事件链继续
     */
    public static ActionResult onAstartesAttack(PlayerEntity player, World world, Hand hand,
                                                Entity entity, EntityHitResult hitResult) {
        // 仅在服务端执行
        if (world.isClient) {
            return ActionResult.PASS;
        }

        // 检查玩家是否拥有贝切尔腺体
        var component = WhcraftComponents.IMPLANT.get(player);
        if (!component.hasImplant(ImplantType.BETCHERS_GLAND)) {
            return ActionResult.PASS;
        }

        // 目标必须是生物实体
        if (!(entity instanceof LivingEntity target)) {
            return ActionResult.PASS;
        }

        // 施加中毒 II（等级 1 = 中毒 II），持续 10 秒 = 200 ticks
        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.POISON,
                200,      // 10 秒
                1,        // 等级 1 -> 中毒 II
                false,    // 无环境来源
                true,     // 显示粒子
                true      // 显示图标
        ));

        return ActionResult.PASS;
    }
}