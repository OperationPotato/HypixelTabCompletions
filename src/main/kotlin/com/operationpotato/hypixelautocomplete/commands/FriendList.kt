package com.operationpotato.hypixelautocomplete.commands

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object FriendList {
    private val pageArgument: RequiredArgumentBuilder<FabricClientCommandSource, Int> =
        argument("page", IntegerArgumentType.integer(1))

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("fl")
            .then(pageArgument)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()
}
