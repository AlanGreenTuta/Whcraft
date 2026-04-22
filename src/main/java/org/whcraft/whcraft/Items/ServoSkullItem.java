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

package org.whcraft.whcraft.Items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.WhcraftEntities;

import java.util.List;

public class ServoSkullItem extends Item {

    // NBT 键名常量
    private static final String HEALTH_KEY = "ServoSkullHealth";
    private static final String NAME_KEY = "ServoSkullName";
    private static final String INVENTORY_KEY = "Inventory";
    private static final String SLOT_KEY = "Slot";
    private static final String ITEM_KEY = "Item";

    // 默认值
    private static final float DEFAULT_HEALTH = 40.0f;
    private static final String DEFAULT_NAME_TRANSLATION_KEY = "entity.whcraft.servo_skull";

    public ServoSkullItem(Settings settings) {
        super(settings);
    }

    /**
     * 将物品 NBT 中的数据应用到实体。
     */
    public void applyToEntity(ItemStack stack, ServoSkullEntity entity) {
        NbtCompound nbt = stack.getOrCreateNbt();

        // 生命值
        if (nbt.contains(HEALTH_KEY, NbtElement.FLOAT_TYPE)) {
            entity.setHealth(nbt.getFloat(HEALTH_KEY));
        } else {
            entity.setHealth(DEFAULT_HEALTH);
        }

        // 自定义名称
        if (nbt.contains(NAME_KEY, NbtElement.STRING_TYPE)) {
            entity.setCustomName(Text.Serializer.fromJson(nbt.getString(NAME_KEY)));
        } else {
            entity.setCustomName(Text.translatable(DEFAULT_NAME_TRANSLATION_KEY));
        }

        // 物品栏
        if (nbt.contains(INVENTORY_KEY, NbtElement.LIST_TYPE)) {
            NbtList invList = nbt.getList(INVENTORY_KEY, NbtElement.COMPOUND_TYPE);
            for (NbtElement elem : invList) {
                NbtCompound slotTag = (NbtCompound) elem;
                int slot = slotTag.getInt(SLOT_KEY);
                ItemStack itemStack = ItemStack.fromNbt(slotTag.getCompound(ITEM_KEY));
                if (!itemStack.isEmpty() && slot >= 0 && slot < entity.getInventory().size()) {
                    entity.getInventory().setStack(slot, itemStack);
                }
            }
        }
    }

    /**
     * 从实体创建物品 NBT 数据。
     */
    public void createFromEntity(ItemStack stack, ServoSkullEntity entity) {
        NbtCompound nbt = stack.getOrCreateNbt();

        // 生命值
        nbt.putFloat(HEALTH_KEY, entity.getHealth());

        // 自定义名称
        if (entity.hasCustomName()) {
            nbt.putString(NAME_KEY, Text.Serializer.toJson(entity.getCustomName()));
        }

        // 物品栏
        NbtList invList = new NbtList();
        for (int i = 0; i < entity.getInventory().size(); i++) {
            ItemStack invStack = entity.getInventory().getStack(i);
            if (!invStack.isEmpty()) {
                NbtCompound slotTag = new NbtCompound();
                slotTag.putInt(SLOT_KEY, i);
                slotTag.put(ITEM_KEY, invStack.writeNbt(new NbtCompound()));
                invList.add(slotTag);
            }
        }
        if (!invList.isEmpty()) {
            nbt.put(INVENTORY_KEY, invList);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.success(stack);
        }

        ServoSkullEntity skull = new ServoSkullEntity(WhcraftEntities.SERVO_SKULL, world);
        skull.setPosition(user.getX(), user.getEyeY() - 0.5, user.getZ());
        skull.setOwner(user);
        applyToEntity(stack, skull);
        world.spawnEntity(skull);
        stack.decrement(1);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.health", DEFAULT_HEALTH).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.name", Text.translatable(DEFAULT_NAME_TRANSLATION_KEY)).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.inventory_empty").formatted(Formatting.GRAY));
            return;
        }

        float health = nbt.contains(HEALTH_KEY, NbtElement.FLOAT_TYPE) ? nbt.getFloat(HEALTH_KEY) : DEFAULT_HEALTH;
        tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.health", String.format("%.1f", health)).formatted(Formatting.GREEN));

        Text name;
        if (nbt.contains(NAME_KEY, NbtElement.STRING_TYPE)) {
            name = Text.Serializer.fromJson(nbt.getString(NAME_KEY));
        } else {
            name = Text.translatable(DEFAULT_NAME_TRANSLATION_KEY);
        }
        tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.name", name).formatted(Formatting.YELLOW));

        if (nbt.contains(INVENTORY_KEY, NbtElement.LIST_TYPE)) {
            NbtList invList = nbt.getList(INVENTORY_KEY, NbtElement.COMPOUND_TYPE);
            int itemCount = invList.size();
            if (itemCount > 0) {
                tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.inventory", itemCount).formatted(Formatting.BLUE));
            } else {
                tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.inventory_empty").formatted(Formatting.GRAY));
            }
        } else {
            tooltip.add(Text.translatable("item.whcraft.servo_skull.tooltip.inventory_empty").formatted(Formatting.GRAY));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isFireproof() {
        return true;
    }
}