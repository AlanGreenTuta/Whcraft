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

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.Items.GeneSeed.GeneSeed;
import org.whcraft.whcraft.Items.STCs.STC;
import org.whcraft.whcraft.Items.ServoSkull.ServoSkullRetrofit;

import java.util.Set;

public class WhcraftItemGroups {
    public static final ItemGroup WHCRAFT_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier("whcraft", "whcraft_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(WhcraftItems.ICON))
                    .displayName(Text.translatable("itemGroup.whcraft.whcraft_group"))
                    .entries((context, entries) -> {
                        //在此添加物品组中的物品（和方块）
                        entries.add(WhcraftItems.ICON);
                        entries.add(WhcraftItems.RAW_LIME);
                        entries.add(WhcraftItems.LIME);
                        entries.add(WhcraftItems.INFERIOR_PLASTEEL_BLANK);
                        entries.add(WhcraftItems.INFERIOR_PLASTEEL);
                    })
                    .build());

    public static final ItemGroup WHCRAFT_NECRON_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier("whcraft", "whcraft_necron_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(WhcraftBlocks.NECRON_TOMB_WALL_BLOCK))
                    .displayName(Text.translatable("itemGroup.whcraft_necron_group"))
                    .entries((context, entries) -> {
                        //在此添加物品组中的物品（和方块）
                        entries.add(WhcraftBlocks.NECRON_TOMB_WALL_BLOCK);
                        entries.add(WhcraftBlocks.NECRON_TOMB_GLOWING_RUNE_A);
                    })
                    .build());

    public static final ItemGroup WHCRAFT_STC_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier("whcraft", "whcraft_stc_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(WhcraftItems.STC))
                    .displayName(Text.translatable("itemGroup.whcraft.whcraft_stc_group"))
                    .entries((context, entries) -> {
                        // 添加空 STC
                        entries.add(new ItemStack(WhcraftItems.STC));
                        // 为每个已知模块添加包含该模块的 STC 物品
                        for (String moduleId : STC.getAllStcModuleIds()) {
                            ItemStack stcWithModule = new ItemStack(WhcraftItems.STC);
                            STC.setStcMods(stcWithModule, Set.of(moduleId));
                            entries.add(stcWithModule);
                        }
                    })
                    .build()
    );

    public static final ItemGroup WHCRAFT_ASTARTES_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier("whcraft", "whcraft_astartes_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(WhcraftItems.GENE_SEED))
                    .displayName(Text.translatable("itemGroup.whcraft.whcraft_astartes_group"))
                    .entries((context, entries) -> {
                        // 空基因种子
                        entries.add(new ItemStack(WhcraftItems.GENE_SEED));
                        // 每个器官单独 1 个种子
                        for (ImplantType type : ImplantType.values()) {
                            ItemStack seed = GeneSeed.createWithImplants(Set.of(type));
                            entries.add(seed);
                        }
                    })
                    .build()
    );

    public static final ItemGroup WHCRAFT_ADEPTUS_MECHANICUS_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier("whcraft", "whcraft_adeptus_mechanicus_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(WhcraftItems.SERVO_SKULL))
                    .displayName(Text.translatable("itemGroup.whcraft.whcraft_adeptus_mechanicus_group"))
                    .entries((context, entries) -> {
                        // 改装
                        for (String id : ServoSkullRetrofit.getAllServoSkullRetrofitIds()) {
                            ItemStack stack = new ItemStack(WhcraftItems.SERVO_SKULL_RETROFIT);
                            ServoSkullRetrofit.setServoSkullRetrofit(stack, id);
                            entries.add(stack);
                        }
                        entries.add(WhcraftItems.CIRCUITRY);
                        entries.add(WhcraftItems.COGITATOR);
                        entries.add(WhcraftItems.ALLOY_SKELETON_SKULL);
                        entries.add(WhcraftItems.SERVO_SKULL);
                        entries.add(WhcraftItems.BROKEN_SERVO_SKULL);
                    })
                    .build()
    );

    public static void initialize() {
    }
}
