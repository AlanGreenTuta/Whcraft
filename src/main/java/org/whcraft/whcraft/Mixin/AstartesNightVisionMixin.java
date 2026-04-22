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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftComponents;
import org.whcraft.whcraft.WhcraftGameRules;

@Mixin(LivingEntity.class)
public abstract class AstartesNightVisionMixin {

    /**
     * 当玩家拥有视眼神经叶器官时，阻止添加任何不符合规则的夜视状态。<br/>
     * 这样药水等来源的夜视会被取消，而器官自身添加的无粒子夜视不受影响。
     */
    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("HEAD"), cancellable = true)
    private void cancelNightVisionParticles(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        // 仅处理服务器端玩家
        if (!(entity instanceof PlayerEntity player) || player.getWorld().isClient) {
            return;
        }

        // 仅处理夜视
        if (effect.getEffectType() != StatusEffects.NIGHT_VISION) {
            return;
        }

        var component = WhcraftComponents.IMPLANT.get(player);
        if (!component.hasImplant(ImplantType.OCCULOBE)) {
            return;
        }

        boolean showIcon = player.getWorld().getGameRules().getBoolean(WhcraftGameRules.SHOW_ASTARTES_STATUS_EFFECT_ICON);

        // 检查是否为带有粒子或图标显示不符合 gamerule 的夜视效果
        if (effect.shouldShowParticles() || effect.shouldShowIcon() != showIcon) {
            cir.setReturnValue(false); // 取消添加该效果
        }
    }
}