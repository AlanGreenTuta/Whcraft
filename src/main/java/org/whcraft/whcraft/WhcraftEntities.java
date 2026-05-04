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

package org.whcraft.whcraft;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullBullet;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullEntity;

public class WhcraftEntities {

    public static final EntityType<ServoSkullEntity> SERVO_SKULL = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("whcraft", "servo_skull"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ServoSkullEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 0.8f))
                    .trackRangeChunks(8)
                    .build()
    );

    public static final EntityType<ServoSkullBullet> SERVO_SKULL_BULLET = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("whcraft", "servo_skull_bullet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC,
                            (EntityType<ServoSkullBullet> type, World world) -> new ServoSkullBullet(type, world))
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .trackRangeChunks(4)
                    .build()
    );

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(SERVO_SKULL, ServoSkullEntity.createServoSkullAttributes());
    }
}