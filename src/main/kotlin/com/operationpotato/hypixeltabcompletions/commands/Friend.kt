package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.commands.arguments.EntityArgument

object Friend {
    private val playerArgument: RequiredArgumentBuilder<FabricClientCommandSource, EntitySelector> =
        argument("player", EntityArgument.player())

    private val friendArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("friend", StringArgumentType.string())

    private val friendNickArguments: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("friend", StringArgumentType.string()).then(
            argument("nickname", StringArgumentType.string())
        )

    private val pageArgument: RequiredArgumentBuilder<FabricClientCommandSource, Int> =
        argument("page", IntegerArgumentType.integer(1))

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("friend")
            .requires(Utils.onHypixel)
            .then(literal("accept").then(playerArgument))
            .then(literal("add").then(playerArgument))
            .then(literal("deny").then(playerArgument))

            .then(literal("best").then(friendArgument))

            .then(literal("nickname").then(friendNickArguments))

            .then(literal("list").then(pageArgument))
            .then(literal("requests").then(pageArgument))

            .then(literal("help"))
            .then(literal("notifications"))
            .then(literal("remove"))
            .then(literal("removeall"))

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("f").redirect(commandNode))
        dispatcher.register(literal("friends").redirect(commandNode))
    }
}
