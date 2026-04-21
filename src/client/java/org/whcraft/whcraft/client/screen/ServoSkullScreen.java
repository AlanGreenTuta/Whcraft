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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullScreenHandler;

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

        // 在标题右侧绘制血量（使用原版心形图标）
        if (skull != null) {
            float health = skull.getHealth();
            float maxHealth = skull.getMaxHealth();
            String healthText = String.format("%.1f/%.1f", health, maxHealth);

            int titleWidth = this.textRenderer.getWidth(this.title);
            int heartX = 8 + titleWidth + 6;
            int heartY = 5;

            // 绘制心形图标（原版生命值纹理，位置 52,0 ，完整的心）
            context.drawTexture(GUI_ICONS_TEXTURE, heartX, heartY, 52, 0, 9, 9);
            // 绘制血量数字
            context.drawText(this.textRenderer, healthText, heartX + 11, 6, 0xFF5555, false);
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