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

import net.minecraft.text.Text;

/**
 * 改造阶段，1~5，包括阶段数值和名称（翻译键）。
 */
public enum Stage {
    ONE(1, Text.translatable("component.astartesOrganImplants.whcraft.stage.one")),
    TWO(2, Text.translatable("component.astartesOrganImplants.whcraft.stage.two")),
    THREE(3, Text.translatable("component.astartesOrganImplants.whcraft.stage.three")),
    FOUR(4, Text.translatable("component.astartesOrganImplants.whcraft.stage.four")),
    FIVE(5, Text.translatable("component.astartesOrganImplants.whcraft.stage.five"));

    public final int level;
    public final Text displayName;

    Stage(int level, Text displayName) {
        this.level=level;
        this.displayName=displayName;
    }

    public static Stage setLevel(int level) {
        for (Stage p : values()) {
            if (p.level == level) return p;
        }
        return ONE;
    }
}
