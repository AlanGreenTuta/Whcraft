package org.whcraft.whcraft.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(Explosion.class)
public abstract class ServoSkullExplosionMixin {

    @ModifyVariable(method = "collectBlocksAndDamageEntities", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
    private List<Entity> filterAndEffectEntities(List<Entity> list) {
        // 获取爆炸源实体
        Explosion self = (Explosion) (Object) this;
        if (self.getEntity() instanceof ServoSkullEntity skull) {
            UUID ownerUuid = skull.getOwnerUuid();
            List<Entity> toRemove = new ArrayList<>();
            for (Entity entity : list) {
                // 过滤主人及主人的宠物
                if (ownerUuid != null) {
                    if (entity instanceof PlayerEntity player && player.getUuid().equals(ownerUuid)) {
                        toRemove.add(entity);
                        continue;
                    }
                    if (entity instanceof TameableEntity tameable && ownerUuid.equals(tameable.getOwnerUuid())) {
                        toRemove.add(entity);
                        continue;
                    }
                    if (entity instanceof ServoSkullEntity otherSkull && ownerUuid.equals(otherSkull.getOwnerUuid())) {
                        toRemove.add(entity);
                        continue;
                    }
                }
                // 施加药水效果
                if (entity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30 * 20, 5));   // 缓慢 VI
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60 * 20, 0));    // 虚弱 I
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40 * 20, 0));      // 凋零 I
                }
            }
            list.removeAll(toRemove);
        }
        return list;
    }
}