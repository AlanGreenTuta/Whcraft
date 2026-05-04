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

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.whcraft.whcraft.Commands.*;

import static net.minecraft.server.command.CommandManager.*;

public class WhcraftCommands {
    public static void initialize() {
        //器官植入命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("implant")
                    .requires(source -> source.hasPermissionLevel(2))
                    // 无参数时显示自身
                    .executes(Implant::showImplants)
                    // 查询指定玩家
                    .then(argument("target", EntityArgumentType.player())
                            .executes(Implant::showPlayerImplants)
                    )
                    // 添加单个
                    .then(literal("add")
                            .then(argument("target", EntityArgumentType.player())
                                    .then(argument("type", StringArgumentType.word())
                                            .suggests(Implant::suggestImplantTypes)
                                            .executes(Implant::addImplant)
                                    )
                            )
                    )
                    // 添加全部
                    .then(literal("addall")
                            .then(argument("target", EntityArgumentType.player())
                                    .executes(Implant::addAllImplants)
                            )
                    )
                    // 移除全部
                    .then(literal("removeall")
                            .then(argument("target", EntityArgumentType.player())
                                    .executes(Implant::removeAllImplants)
                            )
                    )
            );
        });
    }
}