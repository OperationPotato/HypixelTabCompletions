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

object Whisper {
    // todo: friend suggestions
    private val whisperArguments: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("player", StringArgumentType.string())
            .then(argument("message", StringArgumentType.greedyString()))

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("message")
            .requires(Utils.onHypixel)
            .then(whisperArguments)

    // This command + its aliases are not sent by the server at all
    private val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("msg").redirect(commandNode))
        dispatcher.register(literal("whisper").redirect(commandNode))
        dispatcher.register(literal("w").redirect(commandNode))
    }
}
