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

package org.whcraft.whcraft.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullScreenHandler;
import org.whcraft.whcraft.WhcraftItems;

import java.util.Set;
import java.util.stream.Collectors;

public class ServoSkullScreen extends HandledScreen<ServoSkullScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("whcraft", "textures/gui/servo_skull.png");
    private static final Identifier GUI_ICONS_TEXTURE = new Identifier("minecraft", "textures/gui/icons.png");
    private final ServoSkullEntity skull;

    public ServoSkullScreen(ServoSkullScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.skull = handler.getSkull();
        this.backgroundWidth = 176;
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // 标题
        context.drawText(this.textRenderer, this.title, 8, 6, 4210752, false);

        // 在标题右侧绘制血量
        if (skull != null) {
            float health = skull.getHealth();
            float maxHealth = skull.getMaxHealth();
            String healthText = String.format("%.1f/%.1f", health, maxHealth);

            int titleWidth = this.textRenderer.getWidth(this.title);
            int heartX = 8 + titleWidth + 6;
            int heartY = 5;

            // 绘制心形图标
            context.drawTexture(GUI_ICONS_TEXTURE, heartX, heartY, 52, 0, 9, 9);
            // 绘制血量数字
            context.drawText(this.textRenderer, healthText, heartX + 11, 6, 0xFF5555, false);

            // 在血量右侧 4 像素处绘制改装信息
            int retrofitsStartX = heartX + 11 + this.textRenderer.getWidth(healthText) + 4;
            int retrofitsY = 1;

            // 绘制 servo_skull_retrofit 物品图标（使用默认物品模型纹理）
            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack retrofitIcon = new ItemStack(WhcraftItems.SERVO_SKULL_RETROFIT);
            context.drawItem(retrofitIcon, retrofitsStartX, retrofitsY);
            // 图标宽度 16，再空 4 像素
            int textX = retrofitsStartX + 16 + 4;
            int textY = retrofitsY + 5; // 微调垂直居中

            Set<String> refits = skull.getRetrofits();
            // 统计非 null 改装
            Set<String> nonNull = refits.stream().filter(s -> !s.equals("null")).collect(Collectors.toSet());

            if (refits.isEmpty() || (refits.size() == 1 && refits.contains("null"))) {
                // 只有 null 或无改装，显示 null 的翻译名
                Text nullText = Text.translatable("servoskull.retrofit.null");
                context.drawText(this.textRenderer, nullText, textX, textY, 0x5555FF, false);
            } else if (nonNull.size() == 1) {
                String id = nonNull.iterator().next();
                Text retroText = Text.translatable("servoskull.retrofit." + id);
                context.drawText(this.textRenderer, retroText, textX, textY, 0x5555FF, false);
            } else {
                // 多个改装，取第一个非 null 改装 + "..."
                String id = nonNull.iterator().next();
                Text retroText = Text.translatable("servoskull.retrofit." + id).append(Text.literal("..."));
                context.drawText(this.textRenderer, retroText, textX, textY, 0x5555FF, false);
            }
        }

        // 玩家物品栏标题
        context.drawText(this.textRenderer, this.playerInventoryTitle, 8, this.playerInventoryTitleY, 4210752, false);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}