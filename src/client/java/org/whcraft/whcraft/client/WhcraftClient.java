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

package org.whcraft.whcraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.whcraft.whcraft.WhcraftEntities;
import org.whcraft.whcraft.WhcraftScreenHandlers;
import org.whcraft.whcraft.client.model.ServoSkullModel;
import org.whcraft.whcraft.client.renderer.ServoSkullBulletRenderer;
import org.whcraft.whcraft.client.renderer.ServoSkullRenderer;
import org.whcraft.whcraft.client.screen.ServoSkullScreen;

public class WhcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WhcraftClientKeyBindings.initialize();
        WhcraftClientEvents.initialize();

        // 注册模型层
        EntityModelLayerRegistry.registerModelLayer(ServoSkullModel.LAYER_LOCATION, ServoSkullModel::getTexturedModelData);

        // 注册实体渲染器
        EntityRendererRegistry.register(WhcraftEntities.SERVO_SKULL, ServoSkullRenderer::new);
        EntityRendererRegistry.register(WhcraftEntities.SERVO_SKULL_BULLET, ServoSkullBulletRenderer::new);

        // 注册屏幕
        HandledScreens.register(WhcraftScreenHandlers.SERVO_SKULL, ServoSkullScreen::new);
    }
}