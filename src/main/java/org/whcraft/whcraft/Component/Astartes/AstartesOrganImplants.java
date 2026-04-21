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

import dev.onyxstudios.cca.api.v3.component.Component;

import java.util.Set;

/**
 * <p>阿斯塔特改造（器官植入）的接口。</p>
 * <small>注意</small><br />
 * <small>{@code writeToNbt(NbtCompound tag)} 和 {@code readFromNbt(NbtCompound tag)}
 * 已在{@link Component}中声明，实现需要覆写。</small>
 */
public interface AstartesOrganImplants extends Component {

    /**
     * 获取已植入的器官集合。
     * @return 已植入的器官集合
     */
    Set<ImplantType> getImplants();

    /**
     * 是否已植入指定器官。
     * @param type 需要检查是否植入的器官
     * @return true:是 or false:否
     */
    boolean hasImplant(ImplantType type);

    /**
     * 植入指定器官（手术成功时调用）。
     * @param type 要植入的器官
     * @return true:成功 or false:已植入器官
     */
    boolean addImplant(ImplantType type);

    /**
     * 移除所有器官。
     */
    void removeAllImplants();

    /**
     * 获取指定器官冷却的剩余 tick 数。
     * @param type 获取冷却的器官
     * @return -1:指定器官无冷却或未植入 or 自然数:指定器官冷却的剩余 tick 数
     */
    int getCooldown(ImplantType type);

    /**
     * 设置指定器官冷却的剩余 tick 数。
     * @param type 设置冷却的器官
     * @param ticks 设置冷却的 tick 数
     * @return true:成功 or false:指定器官无冷却或未植入
     */
    boolean setCooldown(ImplantType type, int ticks);

    /**
     * 每 tick 递减所有冷却值（服务端调用）。
     */
    void tickCooldowns();

}
