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

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.whcraft.whcraft.WhcraftComponents;

import java.util.*;

/**
 * <p>玩家改造组件实现。</p>
 * <strong>已实现</strong>
 * <ul>
 *     <li>死亡失去所有改造</li>
 *     <li>改造组件</li>
 *     <li>改造冷却逻辑</li>
 * </ul>
 * <strong>未实现</strong>
 * <ul>
 *     <li>改造效果</li>
 *     <li>改造显示 UI</li>
 *     <li>改造器官植入相关</li>
 * </ul>
 */
public class PlayerImplantsComponent implements AstartesOrganImplants, AutoSyncedComponent {

    private final PlayerEntity player;
    private final Set<ImplantType> implants = EnumSet.noneOf(ImplantType.class);
    private final Map<ImplantType, Integer> cooldowns = new EnumMap<>(ImplantType.class);

    /**
     * 标识具有冷却的器官。
     */
    private static final Set<ImplantType> COOLDOWN_TYPES = EnumSet.of(
            ImplantType.SUS_AN_MEMBRANE   // 维生脑膜
    );

    public PlayerImplantsComponent(PlayerEntity player) {
        this.player = player;
    }

    // # AstartesOrganImplants 接口实现

    @Override
    public Set<ImplantType> getImplants() {
        return Collections.unmodifiableSet(implants);
    }

    @Override
    public boolean hasImplant(ImplantType type) {
        return implants.contains(type);
    }

    @Override
    public boolean addImplant(ImplantType type) {
        boolean added = implants.add(type);
        if (added) {
            // 触发同步
            WhcraftComponents.IMPLANT.sync(this.player);
            // 应用第二心脏、骨化腺体、血化蕊、黑色甲壳的属性修改
            AstartesAttributeModification.updateAttributes(this.player);

            AstartesStatusEffectApplicator.updateEffects(this.player);
        }
        return added;
    }

    @Override
    public void removeAllImplants() {
        if (!implants.isEmpty()) {
            implants.clear();
            cooldowns.clear();
            WhcraftComponents.IMPLANT.sync(this.player);
            // 应用第二心脏、骨化腺体、血化蕊、黑色甲壳的属性修改
            AstartesAttributeModification.updateAttributes(this.player);
            AstartesStatusEffectApplicator.updateEffects(this.player);
            // 移除视眼神经叶的夜视效果
            AstartesStatusEffectApplicator.removeNightVision(this.player);
        }
    }

    @Override
    public int getCooldown(ImplantType type) {
        if (!COOLDOWN_TYPES.contains(type) || !hasImplant(type)) {
            return -1;
        }
        return cooldowns.getOrDefault(type, 0);
    }

    @Override
    public boolean setCooldown(ImplantType type, int ticks) {
        if (!COOLDOWN_TYPES.contains(type) || !hasImplant(type)) {
            return false;
        }
        if (ticks <= 0) {
            cooldowns.remove(type);
        } else {
            cooldowns.put(type, ticks);
        }
        WhcraftComponents.IMPLANT.sync(this.player);
        return true;
    }

    @Override
    public void tickCooldowns() {
        if (cooldowns.isEmpty()) return;

        boolean changed = false;
        Iterator<Map.Entry<ImplantType, Integer>> iter = cooldowns.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<ImplantType, Integer> entry = iter.next();
            int newValue = entry.getValue() - 1;
            if (newValue <= 0) {
                iter.remove();
                changed = true;
            } else {
                entry.setValue(newValue);
                changed = true;
            }
        }

        if (changed) {
            WhcraftComponents.IMPLANT.sync(this.player);
        }
    }

    // # Component 接口实现

    @Override
    public void readFromNbt(NbtCompound tag) {
        implants.clear();
        cooldowns.clear();

        // 读取已植入器官列表
        if (tag.contains("implants", NbtElement.LIST_TYPE)) {
            NbtList implantList = tag.getList("implants", NbtElement.STRING_TYPE);
            for (NbtElement elem : implantList) {
                try {
                    ImplantType type = ImplantType.valueOf(elem.asString());
                    implants.add(type);
                } catch (IllegalArgumentException ignored) {
                    // 忽略无效枚举值（可能由版本变更引起）
                }
            }
        }

        // 读取冷却数据
        if (tag.contains("cooldowns", NbtElement.COMPOUND_TYPE)) {
            NbtCompound cooldownTag = tag.getCompound("cooldowns");
            for (ImplantType type : COOLDOWN_TYPES) {
                String key = type.name();
                if (cooldownTag.contains(key, NbtElement.INT_TYPE)) {
                    int ticks = cooldownTag.getInt(key);
                    if (ticks > 0 && hasImplant(type)) {
                        cooldowns.put(type, ticks);
                    }
                }
            }
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // 写入器官列表
        NbtList implantList = new NbtList();
        for (ImplantType type : implants) {
            implantList.add(NbtString.of(type.name()));
        }
        tag.put("implants", implantList);

        // 写入冷却数据
        if (!cooldowns.isEmpty()) {
            NbtCompound cooldownTag = new NbtCompound();
            for (Map.Entry<ImplantType, Integer> entry : cooldowns.entrySet()) {
                cooldownTag.putInt(entry.getKey().name(), entry.getValue());
            }
            tag.put("cooldowns", cooldownTag);
        }
    }

    /**
     * 服务器 tick 调用，处理冷却递减。
     * @param player 玩家实例
     */
    public static void serverTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        AstartesOrganImplants component = WhcraftComponents.IMPLANT.get(player);
        if (component instanceof PlayerImplantsComponent impl) {
            impl.tickCooldowns();
        }
    }
}
