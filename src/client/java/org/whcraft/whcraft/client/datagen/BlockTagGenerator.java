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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.WhcraftBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {

    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // 添加标签
        TagKey<Block> necronDecorative = createTag("c", "necron_decorative_blocks");
        addToTag(necronDecorative,
                WhcraftBlocks.NECRON_TOMB_GLOWING_RUNE_A,
                WhcraftBlocks.NECRON_TOMB_WALL_BLOCK
        );

        TagKey<Block> mineablePickaxe = TagKey.of(RegistryKeys.BLOCK, new Identifier("mineable/pickaxe"));
        getOrCreateTagBuilder(mineablePickaxe)
                .addTag(necronDecorative);

         TagKey<Block> needsDiamondTool = TagKey.of(RegistryKeys.BLOCK, new Identifier("needs_diamond_tool"));
        getOrCreateTagBuilder(needsDiamondTool)
                .addTag(necronDecorative);
    }

    /**
     * 创建 Block 标签键
     */
    public static TagKey<Block> createTag(String namespace, String path) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(namespace, path));
    }

    /**
     * 将多个方块添加到指定标签中
     */
    public void addToTag(TagKey<Block> tag, Block... blocks) {
        var builder = getOrCreateTagBuilder(tag);
        for (Block block : blocks) {
            builder.add(block);
        }
    }
}