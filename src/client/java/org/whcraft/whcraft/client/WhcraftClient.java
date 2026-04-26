package org.whcraft.whcraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.whcraft.whcraft.WhcraftEntities;
import org.whcraft.whcraft.WhcraftScreenHandlers;
import org.whcraft.whcraft.client.model.ServoSkullModel;
import org.whcraft.whcraft.client.renderer.ServoSkullRenderer;
import org.whcraft.whcraft.client.screen.ServoSkullScreen;

public class WhcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // 注册模型层
        EntityModelLayerRegistry.registerModelLayer(ServoSkullModel.LAYER_LOCATION, ServoSkullModel::getTexturedModelData);

        // 注册实体渲染器
        EntityRendererRegistry.register(WhcraftEntities.SERVO_SKULL, ServoSkullRenderer::new);

        //注册 screen
        HandledScreens.register(WhcraftScreenHandlers.SERVO_SKULL, ServoSkullScreen::new);
    }
}
