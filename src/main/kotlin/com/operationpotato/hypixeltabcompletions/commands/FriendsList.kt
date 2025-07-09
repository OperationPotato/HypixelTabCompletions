package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object FriendsList {
    private val pageArgument: RequiredArgumentBuilder<FabricClientCommandSource, Int> =
        argument("page", IntegerArgumentType.integer(1))

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("fl")
            .requires(Utils.onHypixel)
            .then(pageArgument)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()
}
