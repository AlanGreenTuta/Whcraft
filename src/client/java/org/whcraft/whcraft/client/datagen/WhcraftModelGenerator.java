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

package org.whcraft.whcraft.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.WhcraftBlocks;
import org.whcraft.whcraft.WhcraftItems;

import java.util.Optional;

public class WhcraftModelGenerator extends FabricModelProvider {

    public WhcraftModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        // 调用 registerSimpleBlockStateModel
        // 示例：
        // registerSimpleBlockStateModel(generator, WhcraftBlocks.TEST);

        // Blocks
        registerSimpleBlockStateModel(generator, WhcraftBlocks.NECRON_TOMB_GLOWING_RUNE_A);
        registerSimpleBlockStateModel(generator, WhcraftBlocks.NECRON_TOMB_WALL_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // 调用 registerSimpleItemModel 和 registerSimpleBlockItemModel
        // 示例：
        // registerSimpleItemModel(generator, WhcraftItems.TEST);
        // registerSimpleBlockItemModel(generator, WhcraftItems.TEST.asItem());

        // Items
        registerSimpleItemModel(generator, WhcraftItems.ICON);
        registerSimpleItemModel(generator, WhcraftItems.LIME);
        registerSimpleItemModel(generator, WhcraftItems.RAW_LIME);
        registerSimpleItemModel(generator, WhcraftItems.INFERIOR_PLASTEEL);
        registerSimpleItemModel(generator, WhcraftItems.INFERIOR_PLASTEEL_BLANK);
        registerSimpleItemModel(generator, WhcraftItems.GENE_SEED);
        registerSimpleItemModel(generator, WhcraftItems.SERVO_SKULL);
        registerSimpleItemModel(generator, WhcraftItems.BROKEN_SERVO_SKULL);
        registerSimpleItemModel(generator, WhcraftItems.CIRCUITRY);
        registerSimpleItemModel(generator, WhcraftItems.COGITATOR);
        registerSimpleItemModel(generator, WhcraftItems.ALLOY_SKELETON_SKULL);
        registerSimpleItemModel(generator, WhcraftItems.STC);

        // BlockItems
        registerSimpleBlockItemModel(generator, WhcraftBlocks.NECRON_TOMB_GLOWING_RUNE_A.asItem());
        registerSimpleBlockItemModel(generator, WhcraftBlocks.NECRON_TOMB_WALL_BLOCK.asItem());
    }

    /**
     * 注册简单物品模型 (parent: "item/generated", texture layer0)
     */
    public static void registerSimpleItemModel(ItemModelGenerator generator, Item item) {
        generator.register(item, Models.GENERATED);
    }

    /**
     * 注册简单方块状态模型 (parent: "block/cube_all", blockstate variant "")
     */
    public static void registerSimpleBlockStateModel(BlockStateModelGenerator generator, Block block) {
        // 使用原版辅助方法直接生成 cube_all 模型及 blockstate
        generator.registerSimpleCubeAll(block);
    }

    /**
     * 注册 BlockItem 模型，手动指定 parent 为对应方块模型。
     * @param generator 物品模型生成器
     * @param blockItem 要注册的 BlockItem 实例（例如 WhcraftBlocks.XXX.asItem()）
     */
    public static void registerSimpleBlockItemModel(ItemModelGenerator generator, Item blockItem) {
        if (!(blockItem instanceof BlockItem bi)) {
            throw new IllegalArgumentException("Item " + blockItem + " is not a BlockItem");
        }
        Block block = bi.getBlock();
        Identifier blockModelId = ModelIds.getBlockModelId(block);
        Model model = new Model(Optional.of(blockModelId), Optional.empty());
        generator.register(blockItem, model);
    }
}