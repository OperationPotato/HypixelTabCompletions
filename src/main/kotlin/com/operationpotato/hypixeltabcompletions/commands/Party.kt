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
import net.minecraft.command.CommandSource
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType

object Party {
    private val playerArgument: RequiredArgumentBuilder<FabricClientCommandSource, EntitySelector> =
        argument("player", EntityArgumentType.player())

    private val partyMemberSuggestion: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("member", StringArgumentType.string()).suggests { ctx, builder ->
            CommandSource.suggestMatching(Utils.partyMembers, builder)
        }

    private val messageArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("message", StringArgumentType.greedyString())

    private val allInviteSettingLiteral: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("allinvite")

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("party")
            .requires(Utils.onHypixel)
            .then(literal("accept").then(playerArgument))
            .then(literal("invite").then(playerArgument))

            .then(literal("demote").then(partyMemberSuggestion))
            .then(literal("kick").then(partyMemberSuggestion))
            .then(literal("promote").then(partyMemberSuggestion))
            .then(literal("transfer").then(partyMemberSuggestion))

            .then(literal("chat").then(messageArgument))

            .then(literal("settings").then(allInviteSettingLiteral))

            .then(literal("disband"))
            .then(literal("kickoffline"))
            .then(literal("leave"))
            .then(literal("list"))
            .then(literal("mute"))
            .then(literal("poll"))
            .then(literal("private"))
            .then(literal("warp"))

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("p").redirect(commandNode))
    }
}
