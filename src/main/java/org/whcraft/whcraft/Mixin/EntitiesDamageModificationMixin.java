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

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

@Mixin(LivingEntity.class)
public abstract class EntitiesDamageModificationMixin {

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamage(float amount, DamageSource source) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // 伺服头骨特殊免疫
        if (entity instanceof ServoSkullEntity skull) {
            if (source.isIn(DamageTypeTags.IS_FALL)) {
                return 0.0f;
            }
            if (source.isIn(DamageTypeTags.IS_DROWNING)) {
                return 0.0f;
            }
            if (source.isOf(DamageTypes.IN_WALL)) {
                if (skull.getOwner() != null) {
                    skull.recallToOwnerInventory();
                    return 0.0f;
                }
            }

            // 记录攻击者
            Entity directAttacker = source.getSource();
            LivingEntity target = null;
            if (directAttacker instanceof LivingEntity living && living != skull) {
                target = living;
            } else if (directAttacker instanceof ProjectileEntity projectile) {
                Entity owner = projectile.getOwner();
                if (owner instanceof LivingEntity livingOwner && livingOwner != skull) {
                    target = livingOwner;
                }
            }
            if (target != null) {
                skull.addShootTarget(target);
            }
        }

        return amount;
    }
}