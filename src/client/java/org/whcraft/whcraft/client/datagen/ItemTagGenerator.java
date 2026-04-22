package org.whcraft.whcraft.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // 添加标签
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