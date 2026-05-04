package org.whcraft.whcraft.client.renderer;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
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
    public void render(ServoSkullEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(0.0, 0.5, 0.0);
        float entityYaw = entity.getYaw(tickDelta);
        float entityPitch = entity.getPitch(tickDelta);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(0.0F - entityYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entityPitch+180.0F));

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.model.renderBody(matrices, consumer, light, getOverlay(entity, 0));

        if (entity.getRetrofits().contains("weaponry")) {
            this.model.renderWeaponry(matrices, consumer, light, getOverlay(entity, 0));
        }

        matrices.pop();
    }
}