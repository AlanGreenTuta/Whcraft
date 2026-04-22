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
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.WhcraftItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // 添加标签
        TagKey<Item> bones = createTag("c", "bones");
        addToTag(bones, Items.BONE);

        TagKey<Item> circuitries = createTag("c", "circuitries");
        addToTag(circuitries, WhcraftItems.CIRCUITRY);

        TagKey<Item> ingots = createTag("c", "ingots");
        addToTag(ingots, WhcraftItems.INFERIOR_PLASTEEL);

        TagKey<Item> lime = createTag("c", "lime");
        addToTag(lime, WhcraftItems.LIME);

        TagKey<Item> quartzBlocks = createTag("c", "quartz_blocks");
        addToTag(quartzBlocks, Items.QUARTZ_BLOCK);

        TagKey<Item> rawMaterials = createTag("c", "raw_materials");
        addToTag(rawMaterials, WhcraftItems.RAW_LIME);

        TagKey<Item> redstoneBlocks = createTag("c", "redstone_blocks");
        addToTag(redstoneBlocks, Items.REDSTONE_BLOCK);

        TagKey<Item> redstoneDusts = createTag("c", "redstone_dusts");
        addToTag(redstoneDusts, Items.REDSTONE);

        TagKey<Item> skulls = createTag("c", "skulls");
        addToTag(skulls,
                Items.SKELETON_SKULL,
                Items.WITHER_SKELETON_SKULL,
                Items.ZOMBIE_HEAD,
                Items.PLAYER_HEAD,
                Items.CREEPER_HEAD,
                Items.DRAGON_HEAD,
                Items.PIGLIN_HEAD
        );

        TagKey<Item> allCorals = createTag("c", "all_corals");
        addToTag(allCorals,
                Items.TUBE_CORAL,
                Items.BRAIN_CORAL,
                Items.BUBBLE_CORAL,
                Items.FIRE_CORAL,
                Items.HORN_CORAL,
                Items.DEAD_TUBE_CORAL,
                Items.DEAD_BRAIN_CORAL,
                Items.DEAD_BUBBLE_CORAL,
                Items.DEAD_FIRE_CORAL,
                Items.DEAD_HORN_CORAL
        );

        TagKey<Item> allCoralBlocks = createTag("c", "all_coral_blocks");
        addToTag(allCoralBlocks,
                Items.TUBE_CORAL_BLOCK,
                Items.BRAIN_CORAL_BLOCK,
                Items.BUBBLE_CORAL_BLOCK,
                Items.FIRE_CORAL_BLOCK,
                Items.HORN_CORAL_BLOCK,
                Items.DEAD_TUBE_CORAL_BLOCK,
                Items.DEAD_BRAIN_CORAL_BLOCK,
                Items.DEAD_BUBBLE_CORAL_BLOCK,
                Items.DEAD_FIRE_CORAL_BLOCK,
                Items.DEAD_HORN_CORAL_BLOCK
        );

        TagKey<Item> allCoralFans = createTag("c", "all_coral_fans");
        addToTag(allCoralFans,
                Items.TUBE_CORAL_FAN,
                Items.BRAIN_CORAL_FAN,
                Items.BUBBLE_CORAL_FAN,
                Items.FIRE_CORAL_FAN,
                Items.HORN_CORAL_FAN,
                Items.DEAD_TUBE_CORAL_FAN,
                Items.DEAD_BRAIN_CORAL_FAN,
                Items.DEAD_BUBBLE_CORAL_FAN,
                Items.DEAD_FIRE_CORAL_FAN,
                Items.DEAD_HORN_CORAL_FAN
        );

        TagKey<Item> allPlasteels = createTag("whcraft", "all_plasteels");
        addToTag(allPlasteels,
                WhcraftItems.INFERIOR_PLASTEEL,
                WhcraftItems.PLASTEEL
        );

        TagKey<Item> wool = TagKey.of(RegistryKeys.ITEM, new Identifier("wool"));
        addToTag(wool, WhcraftItems.ICON);
    }

    /**
     * 创建 Item 标签键
     */
    public static TagKey<Item> createTag(String namespace, String path) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(namespace, path));
    }

    /**
     * 将多个物品添加到指定标签中
     */
    public void addToTag(TagKey<Item> tag, Item... items) {
        var builder = getOrCreateTagBuilder(tag);
        for (Item item : items) {
            builder.add(item);
        }
    }
}
