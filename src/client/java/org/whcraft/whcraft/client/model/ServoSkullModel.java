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

package org.whcraft.whcraft.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

public class ServoSkullModel extends EntityModel<ServoSkullEntity> {

    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier("whcraft", "servo_skull"), "main");

    private final ModelPart skull;
    private final ModelPart machine;
    private final ModelPart light;

    public ServoSkullModel(ModelPart root) {
        this.skull = root.getChild("skull");
        this.machine = root.getChild("machine");
        this.light = root.getChild("light");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        ModelPartData skull = partdefinition.addChild("skull", ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData inferior = skull.addChild("inferior", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData upper = inferior.addChild("upper", ModelPartBuilder.create()
                        .uv(56, 20).cuboid(-6.0F, 5.0F, 5.0F, 12.0F, 3.0F, 1.0F)
                        .uv(56, 24).cuboid(-6.0F, 5.0F, -5.0F, 12.0F, 3.0F, 1.0F)
                        .uv(50, 48).cuboid(5.0F, 5.0F, -4.0F, 1.0F, 3.0F, 9.0F)
                        .uv(0, 52).cuboid(-6.0F, 5.0F, -4.0F, 1.0F, 3.0F, 9.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData lower = inferior.addChild("lower", ModelPartBuilder.create()
                        .uv(24, 67).cuboid(5.0F, 10.0F, -3.0F, 1.0F, 2.0F, 1.0F)
                        .uv(28, 67).cuboid(5.0F, 10.0F, -1.0F, 1.0F, 2.0F, 1.0F)
                        .uv(32, 67).cuboid(5.0F, 10.0F, 1.0F, 1.0F, 2.0F, 1.0F)
                        .uv(36, 67).cuboid(5.0F, 10.0F, 3.0F, 1.0F, 2.0F, 1.0F)
                        .uv(56, 39).cuboid(5.0F, 8.0F, -3.0F, 1.0F, 2.0F, 7.0F)
                        .uv(56, 28).cuboid(4.0F, 8.0F, -3.0F, 1.0F, 4.0F, 7.0F)
                        .uv(10, 64).cuboid(2.0F, 8.0F, 4.0F, 3.0F, 4.0F, 1.0F)
                        .uv(20, 52).cuboid(0.0F, 8.0F, 4.0F, 2.0F, 2.0F, 1.0F)
                        .uv(60, 66).cuboid(2.0F, 8.0F, -4.0F, 3.0F, 4.0F, 1.0F)
                        .uv(18, 67).cuboid(0.0F, 8.0F, -4.0F, 2.0F, 2.0F, 1.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData calvaria = skull.addChild("calvaria", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6.0F, -3.0F, -5.0F, 12.0F, 1.0F, 11.0F)
                        .uv(0, 12).cuboid(-5.0F, -4.0F, -4.0F, 10.0F, 1.0F, 9.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData superior = skull.addChild("superior", ModelPartBuilder.create()
                        .uv(30, 22).cuboid(-7.0F, -2.0F, -5.0F, 2.0F, 7.0F, 11.0F)
                        .uv(46, 0).cuboid(5.0F, 4.0F, -5.0F, 2.0F, 1.0F, 11.0F)
                        .uv(0, 64).cuboid(5.0F, 1.0F, -1.0F, 2.0F, 3.0F, 3.0F)
                        .uv(0, 38).cuboid(5.0F, -2.0F, -5.0F, 2.0F, 3.0F, 11.0F)
                        .uv(26, 40).cuboid(-7.0F, -2.0F, -6.0F, 14.0F, 7.0F, 1.0F)
                        .uv(38, 12).cuboid(-7.0F, -2.0F, 6.0F, 14.0F, 7.0F, 1.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData machine = partdefinition.addChild("machine", ModelPartBuilder.create()
                        .uv(0, 22).cuboid(-8.0F, -24.0F, -3.0F, 8.0F, 9.0F, 7.0F)
                        .uv(48, 60).cuboid(-9.0F, -23.0F, -2.0F, 2.0F, 5.0F, 4.0F),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        machine.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(32, 61).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                ModelTransform.of(2.0F, -5.0F, -1.0F, 0.0F, 0.0F, 1.1781F));

        machine.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(20, 57).cuboid(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F),
                ModelTransform.of(-4.0F, -7.0F, -1.0F, 0.0F, 0.0F, 0.4363F));

        machine.addChild("cube_r3", ModelPartBuilder.create()
                        .uv(40, 57).cuboid(-1.0F, -9.0F, 3.0F, 2.0F, 11.0F, 2.0F),
                ModelTransform.of(-4.0F, -9.0F, -5.0F, 0.0F, 0.0F, 0.0F));

        ModelPartData light = partdefinition.addChild("light", ModelPartBuilder.create()
                        .uv(60, 60).cuboid(7.0F, -21.0F, -5.0F, 1.0F, 1.0F, 5.0F)
                        .uv(68, 12).cuboid(7.0F, -23.0F, -5.0F, 1.0F, 2.0F, 1.0F)
                        .uv(68, 15).cuboid(7.0F, -23.0F, -1.0F, 1.0F, 2.0F, 1.0F)
                        .uv(20, 61).cuboid(7.0F, -24.0F, -5.0F, 1.0F, 1.0F, 5.0F)
                        .uv(26, 48).cuboid(0.0F, -24.0F, -5.0F, 7.0F, 4.0F, 5.0F),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        return TexturedModelData.of(meshdefinition, 128, 128);
    }

    @Override
    public void setAngles(ServoSkullEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 静态模型，无动画
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        skull.render(matrices, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        machine.render(matrices, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        light.render(matrices, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}