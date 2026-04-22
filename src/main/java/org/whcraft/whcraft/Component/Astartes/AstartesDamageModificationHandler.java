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

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.whcraft.whcraft.WhcraftComponents;

public class AstartesDamageModificationHandler {

    /**
     * 处理阿斯塔特器官带来的伤害减免或免疫效果。<br/>
     * 在玩家受到伤害（护甲计算后）时调用，可修改最终伤害值。
     *
     * @param player 受伤的玩家
     * @param source 伤害来源
     * @param amount 原始伤害量（已扣除护甲）
     * @return 修改后的伤害量
     */
    public static float damageModification(PlayerEntity player, DamageSource source, float amount) {
        // 仅服务端执行
        if (player.getWorld().isClient) {
            return amount;
        }

        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        // 拉瑞曼脏器：免疫坠落伤害
        if (implants.contains(ImplantType.LARRAMANS_ORGAN) && source.isIn(DamageTypeTags.IS_FALL)) {
            return 0.0f;
        }

        // 色素腺体：受到的伤害固定减少 5 点
        if (implants.contains(ImplantType.MELANOCHROME)) {
            float reduced = amount - 5.0f;
            return Math.max(reduced, 0.0f);
        }

        return amount;
    }
}