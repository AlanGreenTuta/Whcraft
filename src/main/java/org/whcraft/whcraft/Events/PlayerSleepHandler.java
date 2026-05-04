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

package org.whcraft.whcraft.Events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.whcraft.whcraft.Component.Astartes.ImplantType;
import org.whcraft.whcraft.Component.Astartes.AstartesOrganImplants;
import org.whcraft.whcraft.Items.GeneSeed.GeneSeed;
import org.whcraft.whcraft.WhcraftComponents;
import org.whcraft.whcraft.WhcraftGameRules;
import org.whcraft.whcraft.WhcraftItems;

import java.util.HashSet;
import java.util.Set;

public class PlayerSleepHandler {

    public static void onAstartesWakeup(LivingEntity entity, BlockPos sleepingPos) {
        if (entity.getWorld().isClient || !(entity instanceof PlayerEntity player)) return;

        World world = player.getWorld();
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        if (!mainHand.isOf(WhcraftItems.SERVO_SKULL) || !offHand.isOf(WhcraftItems.GENE_SEED)) return;

        Set<ImplantType> seedImplants = GeneSeed.getImplantsFromStack(offHand);
        if (seedImplants.isEmpty()) return;

        AstartesOrganImplants component = WhcraftComponents.IMPLANT.get(player);
        Set<ImplantType> currentImplants = component.getImplants();

        // 检查是否全部已拥有
        if (currentImplants.containsAll(seedImplants)) {
            player.sendMessage(Text.translatable("message.whcraft.sleep_implant.no_new").formatted(Formatting.YELLOW), true);
            return; // 不消耗种子
        }

        // 剔除已拥有的器官，得出需要植入的新集
        Set<ImplantType> newImplants = new HashSet<>(seedImplants);
        newImplants.removeAll(currentImplants);

        // 阶段检查
        int maxExistingStage = currentImplants.stream()
                .mapToInt(implant -> implant.stage.level)
                .max().orElse(0);
        int minNewStage = newImplants.stream()
                .mapToInt(implant -> implant.stage.level)
                .min().orElseThrow();

        if (minNewStage > maxExistingStage + 2) {
            player.sendMessage(Text.translatable("message.whcraft.sleep_implant.rejected").formatted(Formatting.RED), true);
            return;
        }

        // 死亡判定
        boolean canFail = world.getGameRules().getBoolean(WhcraftGameRules.CAN_IMPLANTATION_FAIL);
        if (canFail) {
            double surviveChance = 1.0;
            for (ImplantType type : newImplants) {
                surviveChance *= (1.0 - type.baseMortality / 100.0);
            }
            if (world.random.nextFloat() >= surviveChance) {
                consumeSeedIfNeeded(world, offHand);
                player.sendMessage(Text.translatable("message.whcraft.sleep_implant.death").formatted(Formatting.DARK_RED), false);
                player.kill();
                return;
            }
        }

        // 植入新器官
        int added = 0;
        for (ImplantType type : newImplants) {
            if (component.addImplant(type)) added++;
        }
        if (added > 0) {
            player.sendMessage(Text.translatable("message.whcraft.sleep_implant.success", added).formatted(Formatting.GREEN), true);
        } else {
            player.sendMessage(Text.translatable("message.whcraft.sleep_implant.no_new").formatted(Formatting.YELLOW), true);
        }

        consumeSeedIfNeeded(world, offHand);
    }

    /**
     * 根据游戏规则决定是否消耗种子。
     */
    private static void consumeSeedIfNeeded(World world, ItemStack seedStack) {
        boolean keepSeed = world.getGameRules().getBoolean(WhcraftGameRules.KEEP_GENE_SEED_AFTER_IMPLANTATION);
        if (!keepSeed) {
            seedStack.decrement(1);
        }
    }
}