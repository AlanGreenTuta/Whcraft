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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

@Mixin(LivingEntity.class)
public abstract class EntitiesDamageModificationMixin {

    /**
     * 在 applyDamage 方法执行前修改实体受到伤害。
     *
     * @param amount 原始伤害
     * @param source 伤害来源
     * @return 修改后的伤害
     */
    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;
        // 处理伺服头骨免疫摔落伤害
        if (entity instanceof ServoSkullEntity) {
            if (source.isIn(DamageTypeTags.IS_FALL)) {
                return 0.0f;
            }
        }
        return amount;
    }
}