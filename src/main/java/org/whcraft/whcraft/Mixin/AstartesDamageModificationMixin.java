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

package org.whcraft.whcraft.Mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.whcraft.whcraft.Component.Astartes.AstartesDamageModificationHandler;

@Mixin(PlayerEntity.class)
public abstract class AstartesDamageModificationMixin {

    /**
     * 在 applyDamage 方法执行前，修改传入的伤害值 amount。<br/>
     * 通过 Handler 实现器官效果的伤害免疫和减伤。
     *
     * @param amount 原始伤害值（护甲已减免）
     * @param source 伤害来源
     * @return 修改后的伤害值
     */
    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamage(float amount, DamageSource source) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        return AstartesDamageModificationHandler.damageModification(player, source, amount);
    }
}