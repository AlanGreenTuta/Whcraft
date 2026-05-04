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

package org.whcraft.whcraft.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends FabricTagProvider<Biome> {

    public BiomeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        TagKey<Biome> hasCircuitryCogitatorDungeon = createTag("whcraft", "has_structure/circuitry_cogitator_dungeon");
        getOrCreateTagBuilder(hasCircuitryCogitatorDungeon)
                .addOptionalTag(new Identifier("minecraft", "is_overworld"));

        TagKey<Biome> hasServoSkullDungeon = createTag("whcraft", "has_structure/servo_skull_dungeon");
        getOrCreateTagBuilder(hasServoSkullDungeon)
                .addOptionalTag(new Identifier("minecraft", "is_overworld"));
    }

    private static TagKey<Biome> createTag(String namespace, String path) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(namespace, path));
    }
}