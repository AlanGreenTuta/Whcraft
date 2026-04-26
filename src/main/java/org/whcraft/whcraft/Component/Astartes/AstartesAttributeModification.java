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

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.whcraft.whcraft.WhcraftComponents;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 通过属性修改实现第二心脏、骨化腺体、血化蕊、黑色甲壳的效果。
 */
public class AstartesAttributeModification {
    private static final UUID SECONDARY_HEART_UUID =
            UUID.nameUUIDFromBytes("whcraft:attribute/secondary_heart".getBytes(StandardCharsets.UTF_8));
    private static final UUID OSSMODULA_UUID =
            UUID.nameUUIDFromBytes("whcraft:attribute/ossmodula".getBytes(StandardCharsets.UTF_8));
    private static final UUID HAEMASTAMEN_HEALTH_UUID =
            UUID.nameUUIDFromBytes("whcraft:attribute/haemastamen_health".getBytes(StandardCharsets.UTF_8));
    private static final UUID HAEMASTAMEN_ARMOR_UUID =
            UUID.nameUUIDFromBytes("whcraft:attribute/haemastamen_armor".getBytes(StandardCharsets.UTF_8));
    private static final UUID THE_BLACK_CARAPACE_UUID =
            UUID.nameUUIDFromBytes("whcraft:attribute/the_black_carpapace".getBytes(StandardCharsets.UTF_8));

    private static final String SECONDARY_HEART_NAME = "Secondary Heart implant";
    private static final String OSSMODULA_NAME = "Ossmodula implant";
    private static final String HAEMASTAMEN_HEALTH_NAME = "Haemastamen health implant";
    private static final String HAEMASTAMEN_ARMOR_NAME = "Haemastamen armor implant";
    private static final String THE_BLACK_CARAPACE_NAME = "The Black Carapace implant";

    /**
     * <p>根据玩家当前拥有的器官，应用或移除对应的属性修改器。</p>
     * 应在以下时机调用：<br/>
     * - 玩家进入世界（登录、维度切换）<br/>
     * - 玩家重生后<br/>
     * - 组件同步后（即器官集合发生变化时）
     *
     * @param player 目标玩家
     */
    public static void updateAttributes(PlayerEntity player) {
        if (player.getWorld().isClient) return; // 仅服务端执行

        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        // 第二心脏：+20 最大生命值
        applyOrRemoveHealthModifier(player, SECONDARY_HEART_UUID, SECONDARY_HEART_NAME,
                20.0, implants.contains(ImplantType.SECONDARY_HEART));

        // 骨化腺体：+8 护甲值
        applyOrRemoveArmorModifier(player, OSSMODULA_UUID, OSSMODULA_NAME,
                8.0, implants.contains(ImplantType.OSSMODULA));

        // 血化蕊：+20 最大生命值 & +5 护甲值
        boolean hasHaemastamen = implants.contains(ImplantType.HAEMASTAMEN);
        applyOrRemoveHealthModifier(player, HAEMASTAMEN_HEALTH_UUID, HAEMASTAMEN_HEALTH_NAME,
                20.0, hasHaemastamen);
        applyOrRemoveArmorModifier(player, HAEMASTAMEN_ARMOR_UUID, HAEMASTAMEN_ARMOR_NAME,
                5.0, hasHaemastamen);

        // 黑色甲壳：+8 盔甲韧性
        applyOrRemoveToughnessModifier(player, THE_BLACK_CARAPACE_UUID, THE_BLACK_CARAPACE_NAME,
                8.0, implants.contains(ImplantType.THE_BLACK_CARAPACE));

        // 修正因最大生命值变化导致的溢出
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static void applyOrRemoveHealthModifier(PlayerEntity player, UUID uuid, String name,
                                                    double amount, boolean shouldApply) {
        EntityAttributeInstance instance = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (instance == null) return;

        EntityAttributeModifier existing = instance.getModifier(uuid);
        if (shouldApply) {
            if (existing == null) {
                EntityAttributeModifier modifier = new EntityAttributeModifier(
                        uuid, name, amount, EntityAttributeModifier.Operation.ADDITION);
                instance.addTemporaryModifier(modifier); // 使用临时修改器，重启/死亡后需重新应用
            }
            // 如果血量因修改器上限变化而溢出，缩放到新上限
            if (player.getHealth() > player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        } else {
            if (existing != null) {
                instance.removeModifier(uuid);
            }
        }
    }

    private static void applyOrRemoveArmorModifier(PlayerEntity player, UUID uuid, String name,
                                                   double amount, boolean shouldApply) {
        EntityAttributeInstance instance = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (instance == null) return;

        EntityAttributeModifier existing = instance.getModifier(uuid);
        if (shouldApply) {
            if (existing == null) {
                instance.addTemporaryModifier(new EntityAttributeModifier(
                        uuid, name, amount, EntityAttributeModifier.Operation.ADDITION));
            }
        } else {
            if (existing != null) {
                instance.removeModifier(uuid);
            }
        }
    }

    private static void applyOrRemoveToughnessModifier(PlayerEntity player, UUID uuid, String name,
                                                       double amount, boolean shouldApply) {
        EntityAttributeInstance instance = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        if (instance == null) return;

        EntityAttributeModifier existing = instance.getModifier(uuid);
        if (shouldApply) {
            if (existing == null) {
                instance.addTemporaryModifier(new EntityAttributeModifier(
                        uuid, name, amount, EntityAttributeModifier.Operation.ADDITION));
            }
        } else {
            if (existing != null) {
                instance.removeModifier(uuid);
            }
        }
    }

    /**
     * 移除玩家身上所有由本 Mod 添加的属性修改器。
     */
    public static void removeAllModifiers(PlayerEntity player) {
        EntityAttributeInstance health = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (health != null) {
            health.removeModifier(SECONDARY_HEART_UUID);
            health.removeModifier(HAEMASTAMEN_HEALTH_UUID);
        }
        EntityAttributeInstance armor = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR);
        if (armor != null) {
            armor.removeModifier(OSSMODULA_UUID);
            armor.removeModifier(HAEMASTAMEN_ARMOR_UUID);
        }
        EntityAttributeInstance toughness = player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        if (toughness != null) {
            toughness.removeModifier(THE_BLACK_CARAPACE_UUID);
        }

        // 修正因最大生命值变化导致的溢出
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }
}
