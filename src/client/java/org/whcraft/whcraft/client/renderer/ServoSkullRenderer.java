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

package org.whcraft.whcraft.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;
import org.whcraft.whcraft.client.model.ServoSkullModel;

public class ServoSkullRenderer extends MobEntityRenderer<ServoSkullEntity, ServoSkullModel> {

    private static final Identifier TEXTURE = new Identifier("whcraft", "textures/entity/servo_skull.png");

    public ServoSkullRenderer(EntityRendererFactory.Context context) {
        super(context, new ServoSkullModel(context.getPart(ServoSkullModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public Identifier getTexture(ServoSkullEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ServoSkullEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        // 模型向下平移 0.5 格，使碰撞箱视觉上位于模型上半部分
        matrices.translate(0.0, -0.5, 0.0);
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.pop();
    }
}