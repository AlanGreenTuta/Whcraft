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

package org.whcraft.whcraft;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class WhcraftGameRules {
    /**
     * 定义死亡后是否保留阿斯塔特改造。<br/>
     * true: 死亡后保留改造<br/>
     * false: 死亡后不保留，清除改造。（默认）
     */
    public static final GameRules.Key<GameRules.BooleanRule> KEEP_ASTARTES_IMPLANT =
            GameRuleRegistry.register("keepAstartesImplant", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

    /**
     * 定义维生脑膜（Sus-an Membrane）的冷却时间（单位：tick）。<br/>
     * 默认 6000 ticks（300 秒），最小值为 0。
     */
    public static final GameRules.Key<GameRules.IntRule> SUSAN_MEMBRANE_COOLDOWN =
            GameRuleRegistry.register("susanMembraneCooldown", GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(6000, 0));

    /**
     * 定义是否显示阿斯塔特器官提供的状态效果图标。<br/>
     * true: 显示图标<br/>
     * false: 不显示图标（默认）
     */
    public static final GameRules.Key<GameRules.BooleanRule> SHOW_ASTARTES_STATUS_EFFECT_ICON =
            GameRuleRegistry.register("showAstartesStatusEffectIcon", GameRules.Category.PLAYER,
                    GameRuleFactory.createBooleanRule(false));

    /**
     * 定义阿斯塔特器官状态效果的持续时间（单位：tick）。默认 200 ticks。
     */
    public static final GameRules.Key<GameRules.IntRule> ASTARTES_STATUS_EFFECT_DURATION =
            GameRuleRegistry.register("astartesStatusEffectDuration", GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(200, 1));

    /**
     * 定义阿斯塔特器官状态效果刷新阈值（单位：tick）。当剩余时间低于此值时才会续期。默认 10 ticks。
     */
    public static final GameRules.Key<GameRules.IntRule> ASTARTES_STATUS_EFFECT_THRESHOLD =
            GameRuleRegistry.register("astartesStatusEffectThreshold", GameRules.Category.PLAYER,
                    GameRuleFactory.createIntRule(10, 0));

    public static void initialize() {

    }
}
