package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object Reply {
    private val messageArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("message", StringArgumentType.greedyString())

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("r")
            .requires(Utils.onHypixel)
            .then(messageArgument)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("reply").redirect(commandNode))
    }
}
