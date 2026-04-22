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
 * 所有的改造（植入）类型，包括名称（翻译键）、阶段和死亡率。
 */
public enum ImplantType {
    SECONDARY_HEART(Text.translatable("component.astartesOrganImplants.whcraft.implantType.SECONDARY_HEART"), Stage.ONE,0),
    OSSMODULA(Text.translatable("component.astartesOrganImplants.whcraft.implantType.OSSMODULA"), Stage.ONE,5),
    BISCOPEA(Text.translatable("component.astartesOrganImplants.whcraft.implantType.BISCOPEA"), Stage.ONE,20),
    HAEMASTAMEN(Text.translatable("component.astartesOrganImplants.whcraft.implantType.HAEMASTAMEN"), Stage.TWO,10),
    LARRAMANS_ORGAN(Text.translatable("component.astartesOrganImplants.whcraft.implantType.LARRAMANS_ORGAN"), Stage.TWO,20),
    CATALEPSEAN_NODE(Text.translatable("component.astartesOrganImplants.whcraft.implantType.CATALEPSEAN_NODE"), Stage.TWO,10),
    PREOMNOR(Text.translatable("component.astartesOrganImplants.whcraft.implantType.PREOMNOR"), Stage.THREE,10),
    OMOPHAGEA(Text.translatable("component.astartesOrganImplants.whcraft.implantType.OMOPHAGEA"), Stage.THREE,10),
    MULTI_LUNG(Text.translatable("component.astartesOrganImplants.whcraft.implantType.MULTI_LUNG"), Stage.THREE,10),
    OCCULOBE(Text.translatable("component.astartesOrganImplants.whcraft.implantType.OCCULOBE"), Stage.THREE,10),
    LYMANS_EAR(Text.translatable("component.astartesOrganImplants.whcraft.implantType.LYMANS_EAR"), Stage.THREE,10),
    SUS_AN_MEMBRANE(Text.translatable("component.astartesOrganImplants.whcraft.implantType.SUS_AN_MEMBRANE"), Stage.THREE,40),
    MELANOCHROME(Text.translatable("component.astartesOrganImplants.whcraft.implantType.MELANOCHROME"), Stage.FOUR,10),
    OOLITIC_KIDNEY(Text.translatable("component.astartesOrganImplants.whcraft.implantType.OOLITIC_KIDNEY"), Stage.FOUR,10),
    NEUROGLOTTIS(Text.translatable("component.astartesOrganImplants.whcraft.implantType.NEUROGLOTTIS"), Stage.FOUR,10),
    MUCRANOID(Text.translatable("component.astartesOrganImplants.whcraft.implantType.MUCRANOID"), Stage.FIVE,20),
    BETCHERS_GLAND(Text.translatable("component.astartesOrganImplants.whcraft.implantType.BETCHERS_GLAND"), Stage.FIVE,5),
    PROGENOID_GLANDS(Text.translatable("component.astartesOrganImplants.whcraft.implantType.PROGENOID_GLANDS"), Stage.FIVE,0),
    THE_BLACK_CARAPACE(Text.translatable("component.astartesOrganImplants.whcraft.implantType.THE_BLACK_CARAPACE"), Stage.FIVE,10);

    public final Text displayName;    //器官名称
    public final Stage stage;    //所属改造阶段
    public final float baseMortality;    // 基础死亡率 (%)

    ImplantType(Text displayName, Stage stage, float baseMortality) {
        this.displayName=displayName;
        this.stage=stage;
        this.baseMortality=baseMortality;
    }

}
