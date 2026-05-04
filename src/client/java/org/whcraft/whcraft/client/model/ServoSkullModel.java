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

    public static final EntityModelLayer LAYER_LOCATION =
            new EntityModelLayer(new Identifier("whcraft", "servo_skull"), "main");

    private final ModelPart weapon;
    private final ModelPart gun_main;
    private final ModelPart skull;
    private final ModelPart machine;
    private final ModelPart light;

    public ServoSkullModel(ModelPart root) {
        this.weapon = root.getChild("weapon");
        this.gun_main = this.weapon.getChild("gun_main");
        this.skull = root.getChild("skull");
        this.machine = root.getChild("machine");
        this.light = root.getChild("light");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData meshdefinition = new ModelData();
        ModelPartData partdefinition = meshdefinition.getRoot();

        // 武装部分
        ModelPartData weapon = partdefinition.addChild("weapon", ModelPartBuilder.create()
                        .uv(38, 36).cuboid(4.0F, -7.0F, 5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                        .uv(42, 36).cuboid(5.0F, -8.0F, 5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                        .uv(46, 36).cuboid(4.0F, -9.0F, 5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                        .uv(52, 50).cuboid(3.0F, -8.0F, 5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                        .uv(56, 47).cuboid(6.0F, -9.0F, -9.0F, 1.0F, 3.0F, 10.0F, new Dilation(0.0F))
                        .uv(26, 60).cuboid(2.0F, -9.0F, -9.0F, 1.0F, 3.0F, 10.0F, new Dilation(0.0F))
                        .uv(52, 36).cuboid(3.0F, -6.0F, -9.0F, 3.0F, 1.0F, 10.0F, new Dilation(0.0F))
                        .uv(0, 56).cuboid(3.0F, -10.0F, -9.0F, 3.0F, 1.0F, 10.0F, new Dilation(0.0F)),
                ModelTransform.pivot(2.0F, 23.0F, 2.0F));

        weapon.addChild("cube_r1", ModelPartBuilder.create()
                        .uv(68, 32).cuboid(-9.0F, -1.0F, -1.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.of(4.0F, -9.0F, 3.0F, 0.0F, 0.0F, 0.8727F));

        weapon.addChild("gun_main", ModelPartBuilder.create()
                        .uv(0, 12).cuboid(5.0F, -10.0F, -6.0F, 3.0F, 3.0F, 13.0F, new Dilation(0.0F)),
                ModelTransform.pivot(-2.0F, 1.0F, -2.0F));

        // 头骨部分
        ModelPartData skull = partdefinition.addChild("skull", ModelPartBuilder.create(),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData inferior = skull.addChild("inferior", ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        inferior.addChild("upper", ModelPartBuilder.create()
                        .uv(62, 24).cuboid(-6.0F, 5.0F, 5.0F, 12.0F, 3.0F, 1.0F, new Dilation(0.0F))
                        .uv(68, 28).cuboid(-6.0F, 5.0F, -5.0F, 12.0F, 3.0F, 1.0F, new Dilation(0.0F))
                        .uv(62, 12).cuboid(5.0F, 5.0F, -4.0F, 1.0F, 3.0F, 9.0F, new Dilation(0.0F))
                        .uv(0, 67).cuboid(-6.0F, 5.0F, -4.0F, 1.0F, 3.0F, 9.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        inferior.addChild("lower", ModelPartBuilder.create()
                        .uv(52, 47).cuboid(5.0F, 10.0F, -3.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(78, 51).cuboid(5.0F, 10.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(78, 54).cuboid(5.0F, 10.0F, 1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(78, 57).cuboid(5.0F, 10.0F, 3.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(64, 69).cuboid(5.0F, 8.0F, -3.0F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F))
                        .uv(48, 69).cuboid(4.0F, 8.0F, -3.0F, 1.0F, 4.0F, 7.0F, new Dilation(0.0F))
                        .uv(78, 41).cuboid(2.0F, 8.0F, 4.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F))
                        .uv(20, 67).cuboid(0.0F, 8.0F, 4.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(78, 46).cuboid(2.0F, 8.0F, -4.0F, 3.0F, 4.0F, 1.0F, new Dilation(0.0F))
                        .uv(20, 70).cuboid(0.0F, 8.0F, -4.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        skull.addChild("calvaria", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-6.0F, -3.0F, -5.0F, 12.0F, 1.0F, 11.0F, new Dilation(0.0F))
                        .uv(0, 28).cuboid(-5.0F, -4.0F, -4.0F, 10.0F, 1.0F, 9.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        skull.addChild("superior", ModelPartBuilder.create()
                        .uv(0, 38).cuboid(-7.0F, -2.0F, -5.0F, 2.0F, 7.0F, 11.0F, new Dilation(0.0F))
                        .uv(46, 0).cuboid(5.0F, 4.0F, -5.0F, 2.0F, 1.0F, 11.0F, new Dilation(0.0F))
                        .uv(78, 35).cuboid(5.0F, 1.0F, -1.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F))
                        .uv(26, 38).cuboid(5.0F, -2.0F, -5.0F, 2.0F, 3.0F, 11.0F, new Dilation(0.0F))
                        .uv(38, 28).cuboid(-7.0F, -2.0F, -6.0F, 14.0F, 7.0F, 1.0F, new Dilation(0.0F))
                        .uv(26, 52).cuboid(-7.0F, -2.0F, 6.0F, 14.0F, 7.0F, 1.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        // 机械部分
        ModelPartData machine = partdefinition.addChild("machine", ModelPartBuilder.create()
                        .uv(32, 12).cuboid(-8.0F, -24.0F, -3.0F, 8.0F, 9.0F, 7.0F, new Dilation(0.0F))
                        .uv(20, 73).cuboid(-5.0F, -18.0F, -2.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F))
                        .uv(72, 60).cuboid(-9.0F, -23.0F, -2.0F, 2.0F, 5.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        machine.addChild("cube_r2", ModelPartBuilder.create()
                        .uv(40, 73).cuboid(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.of(2.0F, -5.0F, -1.0F, 0.0F, 0.0F, 1.1781F));

        machine.addChild("cube_r3", ModelPartBuilder.create()
                        .uv(72, 0).cuboid(-1.0F, -2.0F, -1.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F)),
                ModelTransform.of(-4.0F, -7.0F, -1.0F, 0.0F, 0.0F, 0.4363F));

        // 灯光部分
        partdefinition.addChild("light", ModelPartBuilder.create()
                        .uv(72, 4).cuboid(7.0F, -21.0F, -5.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                        .uv(64, 78).cuboid(7.0F, -23.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(68, 78).cuboid(7.0F, -23.0F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                        .uv(28, 73).cuboid(7.0F, -24.0F, -5.0F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F))
                        .uv(48, 60).cuboid(0.0F, -24.0F, -5.0F, 7.0F, 4.0F, 5.0F, new Dilation(0.0F)),
                ModelTransform.of(0.0F, 24.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        return TexturedModelData.of(meshdefinition, 128, 128);
    }

    @Override
    public void setAngles(ServoSkullEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
    }

    /**
     * 渲染主体
     */
    public void renderBody(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay) {
        skull.render(matrices, vertexConsumer, light, overlay);
        machine.render(matrices, vertexConsumer, light, overlay);
        this.light.render(matrices, vertexConsumer, light, overlay);
    }

    /**
     * 仅渲染武装附件
     */
    public void renderWeaponry(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay) {
        weapon.render(matrices, vertexConsumer, light, overlay);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        renderBody(matrices, vertexConsumer, light, overlay);
    }
}