package com.operationpotato.hypixelautocomplete.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType

object Guild {
    private val messageArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("message", StringArgumentType.greedyString())

    private val guildNameArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("name", StringArgumentType.greedyString())

    private val playerArgument: RequiredArgumentBuilder<FabricClientCommandSource, EntitySelector> =
        argument("player", EntityArgumentType.player())

    private val memberArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("member", StringArgumentType.string())

    private val memberRankArguments: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("member", StringArgumentType.string()).then(
            argument("rank", StringArgumentType.greedyString())
        )

    private val memberKickReasonArguments: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("member", StringArgumentType.string()).then(
            argument("reason", StringArgumentType.greedyString())
        )

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("guild")
            .then(literal("chat").then(messageArgument))

            .then(literal("create").then(guildNameArgument))
            .then(literal("rename").then(guildNameArgument))
            .then(literal("join").then(guildNameArgument))

            .then(literal("invite").then(playerArgument))

            .then(literal("demote").then(memberArgument))
            .then(literal("promote").then(memberArgument))
            .then(literal("transfer").then(memberArgument))

            .then(literal("kick").then(memberKickReasonArguments))

            .then(literal("setrank").then(memberRankArguments))

            .then(literal("mute").then(memberArgument))
            .then(literal("unmute").then(memberArgument))

            .then(literal("accept"))
            .then(literal("disband"))
            .then(literal("discord"))
            .then(literal("help"))
            .then(literal("history"))
            .then(literal("info"))
            .then(literal("leave"))
            .then(literal("log"))
            .then(literal("member"))
            .then(literal("members"))
            .then(literal("menu"))
            .then(literal("motd"))
            .then(literal("mypermissions"))
            .then(literal("notifications"))
            .then(literal("officerchat"))
            .then(literal("online"))
            .then(literal("onlinemode"))
            .then(literal("party"))
            .then(literal("permissions"))
            .then(literal("quest"))
            .then(literal("settings"))
            .then(literal("slow"))
            .then(literal("tag"))
            .then(literal("tagcolor"))
            .then(literal("toggle"))
            .then(literal("top"))

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("g").redirect(commandNode))
    }
}
