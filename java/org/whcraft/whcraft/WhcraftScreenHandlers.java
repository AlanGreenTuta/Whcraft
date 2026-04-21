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

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.whcraft.whcraft.Entities.ServoSkull.ServoSkullScreenHandler;

public class WhcraftScreenHandlers {
    public static final ScreenHandlerType<ServoSkullScreenHandler> SERVO_SKULL =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier("whcraft", "servo_skull"),
                    new ExtendedScreenHandlerType<>(ServoSkullScreenHandler::new));

    public static void initialize() {
        // 静态初始化自动注册
    }
}