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

package org.whcraft.whcraft.Entities.ServoSkull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.whcraft.whcraft.WhcraftEntities;
import org.whcraft.whcraft.WhcraftGameRules;

public class ServoSkullBullet extends ProjectileEntity {

    private int life = 120;                      // 存活 120 ticks (6秒)
    private final ServoSkullEntity shooter;     // 发射者

    public ServoSkullBullet(EntityType<? extends ServoSkullBullet> type, World world) {
        super(type, world);
        this.shooter = null;
        this.noClip = false;
    }

    public ServoSkullBullet(World world, ServoSkullEntity shooter) {
        super(WhcraftEntities.SERVO_SKULL_BULLET, world);
        this.shooter = shooter;
        this.noClip = false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {

        } else {
            life--;
            if (life <= 0) {
                this.discard();
                return;
            }

            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
        }
    }

    protected boolean canHit(Entity entity) {
        if (entity == this.shooter) return false;
        if (this.shooter != null) {
            if (entity instanceof ServoSkullEntity skull &&
                    skull.getOwnerUuid() != null &&
                    skull.getOwnerUuid().equals(this.shooter.getOwnerUuid())) {
                return false;
            }
            Entity shooterOwner = this.shooter.getOwner();
            if (shooterOwner != null) {
                if (entity == shooterOwner) return false;
                if (entity instanceof TameableEntity tame &&
                        tame.getOwnerUuid() != null &&
                        tame.getOwnerUuid().equals(shooterOwner.getUuid())) {
                    return false;
                }
            }
        }
        return entity instanceof LivingEntity;
    }

    protected void onCollision(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            if (entity instanceof LivingEntity target) {
                float damage = (float) this.getWorld().getGameRules().getInt(WhcraftGameRules.SERVO_SKULL_ATTACK_DAMAGE);
                if (this.shooter != null) {
                    target.damage(
                            this.getWorld().getDamageSources().mobProjectile(this, this.shooter),
                            damage
                    );
                } else {
                    target.damage(this.getWorld().getDamageSources().magic(), damage);
                }
            }
        }
        this.discard();
    }

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}
}