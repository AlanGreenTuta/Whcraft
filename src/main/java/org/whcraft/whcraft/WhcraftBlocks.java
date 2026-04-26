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

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class WhcraftBlocks {

    public static final Block NECRON_TOMB_WALL_BLOCK = register("necron_tomb_wall_block", new Block(Block.Settings.create().strength(50.0f, 1200.0f).requiresTool()));
    public static final Block NECRON_TOMB_GLOWING_RUNE_A = register("necron_tomb_glowing_rune_a", new Block(Block.Settings.create().strength(50.0f, 1200.0f).requiresTool().luminance(state -> 15)));

    /**
     * <p>方块通用注册方法，会同时注册 BlockItem 。</p>
     * 示例：{@code public static final Block EXAMPLE= register("example", new Block(new Block.Settings()));}
     * <br/>{@code new Block(...)} 可以替换为相应物品类。
     * <br/>{@code new Block.Settings()} 可以通过在此处调用其成员方法设置一些简单的物品属性，无需方块类。
     * <br/>
     * <table border="1" summary="方法表格">
     *     <caption><strong>可用方法（不完整）</strong></caption>
     *     <tr>
     *         <th>方法</th>
     *         <th>用途</th>
     *         <th>示例</th>
     *     </tr>
     *     <tr>
     *         <th>{@code requiresTool}</th>
     *         <th>标记方块需要正确的工具（在tag中定义）才能掉落</th>
     *         <th>{@code .requiresTool()}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code strength}</th>
     *         <th>设置方块的硬度和抗性<br/>硬度决定了方块需要多久挖掘<br/>抗性决定了方块抵御爆破伤害（如爆炸）的能力</th>
     *         <th>黑曜石 {@code .strength(50.0f, 1200.0f)}<br/>石头 {@code .strength(1.5f, 6.0f)}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code luminance}</th>
     *         <th>设置方块的光照等级（0-15）</th>
     *         <th>光照等级15 {@code .luminance(state -> 15)}</th>
     *     </tr>
     * </block>
     * @param path 注册路径，即方块 id
     * @param block 方块类，如无可填 {@code Block} ，详见示例
     * @return 方块注册实例
     * @param <T>
     */
    private static <T extends Block> T register(String path, T block) {
        Registry.register(Registries.BLOCK, Identifier.of("whcraft", path), block);
        Registry.register(Registries.ITEM, Identifier.of("whcraft", path), new BlockItem(block, new Item.Settings()));
        return block;
    }

    public static void initialize() {
    }

}