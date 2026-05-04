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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.WhcraftEntities;

@Mixin(PlayerEntity.class)
public abstract class PlayerDamageModificationMixin {

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void onPlayerDamage(DamageSource source, float amount, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.getWorld().isClient) return;

        Entity directAttacker = source.getSource();
        LivingEntity target = null;
        if (directAttacker instanceof LivingEntity living && living != player) {
            target = living;
        } else if (directAttacker instanceof ProjectileEntity projectile) {
            Entity owner = projectile.getOwner();
            if (owner instanceof LivingEntity livingOwner && livingOwner != player) {
                target = livingOwner;
            }
        }
        if (target != null) {
            for (ServoSkullEntity skull : ((ServerWorld) player.getWorld()).getEntitiesByType(
                    WhcraftEntities.SERVO_SKULL,
                    skull -> player.getUuid().equals(skull.getOwnerUuid()))) {
                skull.addShootTarget(target);
            }
        }
    }
}