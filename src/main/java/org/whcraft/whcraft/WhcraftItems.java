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

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Items.*;
import org.whcraft.whcraft.Items.GeneSeed.GeneSeed;
import org.whcraft.whcraft.Items.GeneSeed.GeneSeedIngredient;
import org.whcraft.whcraft.Items.STCs.STC;
import org.whcraft.whcraft.Items.STCs.StcModuleIngredient;

public class WhcraftItems {
    //物品注册
    public static final Item ICON = register("icon", new Icon(new Item.Settings()));
    public static final Item RAW_LIME = register("raw_lime", new RawLime(new Item.Settings()));
    public static final Item LIME = register("lime", new Lime(new Item.Settings()));
    public static final Item INFERIOR_PLASTEEL = register("inferior_plasteel", new InferiorPlasteel(new Item.Settings()));
    public static final Item INFERIOR_PLASTEEL_BLANK = register("inferior_plasteel_blank", new InferiorPlasteelBlank(new Item.Settings()));
    public static final Item GENE_SEED = register("gene_seed", new GeneSeed(new Item.Settings().maxCount(1).fireproof()));
    public static final Item STC = register("stc", new STC(new Item.Settings().maxCount(1).fireproof()));
    public static final Item SERVO_SKULL = register("servo_skull", new ServoSkullItem(new Item.Settings().maxCount(1).fireproof()));
    public static final Item BROKEN_SERVO_SKULL = register("broken_servo_skull", new BrokenServoSkullItem(new Item.Settings().maxCount(1).fireproof()));
    public static final Item CIRCUITRY = register("circuitry", new Circuitry(new Item.Settings()));
    public static final Item COGITATOR = register("cogitator", new Cogitator(new Item.Settings().maxCount(32)));
    public static final Item ALLOY_SKELETON_SKULL = register("alloy_skeleton_skull", new AlloySkeletonSkull(new Item.Settings().maxCount(1)));
    public static final Item PLASTEEL = register("plasteel", new Item(new Item.Settings())); //占位

    /**
     * <p>物品通用注册方法。</p>
     * 示例：{@code public static final Item EXAMPLE= register("example", new Item(new Item.Settings()));}
     * <br/>{@code new Item(...)} 可以替换为相应物品类。
     * <br/>{@code new Item.Settings()} 可以通过在此处调用其成员方法设置一些简单的物品属性，无需物品类。
     * @param path 注册路径，即物品 id
     * @param item 物品类，如无可填 {@code Item} ，详见示例
     * @return 物品注册实例
     * @param <T>
     */
    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, new Identifier("whcraft", path), item);
    }

    /**
     * <p>堆肥功能注册。</p>
     * 示例：{@code CompostingChanceRegistry.INSTANCE.add(EXAMPLE, 0.1f);}
     * <br/>{@code 0.1f} 指使用此物品堆肥时，有0.1的概率使堆肥进度增加。
     */
    public static void registerCompostingChance() {
        CompostingChanceRegistry.INSTANCE.add(RAW_LIME, 0.85f);
        CompostingChanceRegistry.INSTANCE.add(LIME, 0.9f);
    }

    /**
     * Ingredient 序列化器注册
     */
    public static void registerCustomIngredient() {
        CustomIngredientSerializer.register(StcModuleIngredient.Serializer.INSTANCE);
        CustomIngredientSerializer.register(GeneSeedIngredient.Serializer.INSTANCE);
    }

    public static void initialize() {
        registerCompostingChance();
        registerCustomIngredient();
    }
}
