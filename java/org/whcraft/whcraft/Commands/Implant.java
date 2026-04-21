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

package org.whcraft.whcraft.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.WhcraftComponents;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Implant {
    /**
     * 显示自身改造信息。
     * @param context 默认传参
     * @return 0 or 1
     */
    public static int showImplants(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        if (!source.isExecutedByPlayer()) {
            source.sendError(Text.translatable("commands.whcraft.implant.show.error"));
            return 0;
        }
        ServerPlayerEntity player = source.getPlayer();
        if(player == null) return 0;
        var component = WhcraftComponents.IMPLANT.get(player);
        var implants = component.getImplants();

        if (implants.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.whcraft.implant.show.empty").formatted(Formatting.YELLOW), false);
        } else {
            String list = implants.stream()
                    .map(type -> type.displayName.getString())
                    .collect(Collectors.joining(", "));
            source.sendFeedback(() -> Text.translatable("commands.whcraft.implant.show.list", list).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    /**
     * 显示指定玩家的改造信息。
     * @param context 命令上下文
     * @return 执行结果
     * @throws CommandSyntaxException 当目标玩家不存在时抛出
     */
    public static int showPlayerImplants(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        var component = WhcraftComponents.IMPLANT.get(target);
        var implants = component.getImplants();

        if (implants.isEmpty()) {
            source.sendFeedback(() -> Text.translatable("commands.whcraft.implant.show.empty.player", target.getDisplayName()).formatted(Formatting.YELLOW), false);
        } else {
            String list = implants.stream()
                    .map(type -> type.displayName.getString())
                    .collect(Collectors.joining(", "));
            source.sendFeedback(() -> Text.translatable("commands.whcraft.implant.show.list.player", target.getDisplayName(), list).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    /**
     * 添加改造。
     * @param context 默认传参
     * @return 0 or 1
     * @throws CommandSyntaxException {@code player} 为 {@code null}。
     */
    public static int addImplant(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        String typeName = StringArgumentType.getString(context, "type");

        ImplantType type;
        try {
            type = ImplantType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendError(Text.translatable("commands.whcraft.implant.add.error", typeName));
            return 0;
        }

        var component = WhcraftComponents.IMPLANT.get(target);
        boolean added = component.addImplant(type);

        if (added) {
            source.sendFeedback(() -> Text.translatable(
                    "commands.whcraft.implant.add.success",
                    target.getDisplayName(),
                    type.displayName
            ).formatted(Formatting.GREEN), true);
        } else {
            source.sendFeedback(() -> Text.translatable(
                    "commands.whcraft.implant.add.failed",
                    target.getDisplayName()
            ).formatted(Formatting.RED), true);
        }
        return 1;
    }

    /**
     * 为指定玩家添加所有改造。
     * @param context 命令上下文
     * @return 执行结果
     * @throws CommandSyntaxException 当目标玩家不存在时抛出
     */
    public static int addAllImplants(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        var component = WhcraftComponents.IMPLANT.get(target);

        int addedCount = 0;
        for (ImplantType type : ImplantType.values()) {
            if (component.addImplant(type)) {
                addedCount++;
            }
        }

        final int finalAddedCount = addedCount; // 确保 lambda 中使用的是 effectively final 变量
        if (finalAddedCount > 0) {
            source.sendFeedback(() -> Text.translatable(
                    "commands.whcraft.implant.addall.success",
                    finalAddedCount,
                    target.getDisplayName()
            ).formatted(Formatting.GREEN), true);
        } else {
            source.sendFeedback(() -> Text.translatable(
                    "commands.whcraft.implant.addall.nonenew",
                    target.getDisplayName()
            ).formatted(Formatting.YELLOW), true);
        }
        return 1;
    }

    /**
     * 移除所有改造。
     * @param context 默认传参
     * @return 0 or 1
     * @throws CommandSyntaxException {@code player} 为 {@code null}。
     */
    public static int removeAllImplants(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");

        var component = WhcraftComponents.IMPLANT.get(target);
        component.removeAllImplants();

        source.sendFeedback(() -> Text.translatable(
                "commands.whcraft.implant.removeall.success",
                target.getDisplayName()
        ).formatted(Formatting.GREEN), true);
        return 1;
    }

    /**
     * 自动补全建议。
     * @param context 默认传参
     * @param builder 默认传参
     * @return 补全
     */
    public static CompletableFuture<Suggestions> suggestImplantTypes(
            CommandContext<ServerCommandSource> context,
            SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(
                Arrays.stream(ImplantType.values()).map(Enum::name),
                builder
        );
    }
}