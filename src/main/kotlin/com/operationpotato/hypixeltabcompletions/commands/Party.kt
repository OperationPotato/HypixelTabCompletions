package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.PartyUtils
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
            PartyUtils.requestPartyInfo()
            CommandSource.suggestMatching(PartyUtils.partyMembers, builder)
        }

    private val messageArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("message", StringArgumentType.greedyString())

    private val allInviteSettingLiteral: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("allinvite")

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("party")
            .requires(Utils.onHypixel)
            .then(literal("accept").then(playerArgument))
            .then(literal("invite").then(playerArgument).requires(PartyUtils.partyModerator))

            .then(literal("demote").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))
            .then(literal("kick").then(partyMemberSuggestion).requires(PartyUtils.partyModerator))
            .then(literal("promote").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))
            .then(literal("transfer").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))

            .then(literal("chat").then(messageArgument))

            .then(literal("settings").requires(PartyUtils.partyLeader).then(allInviteSettingLiteral))

            .then(literal("disband").requires(PartyUtils.partyLeader))
            .then(literal("kickoffline").requires(PartyUtils.partyLeader))
            .then(literal("leave"))
            .then(literal("list"))
            .then(literal("mute").requires(PartyUtils.partyModerator))
            .then(literal("poll").requires(PartyUtils.MVPPlusPlusPerks))
            .then(literal("private").requires(PartyUtils.MVPPlusPlusPerks))
            .then(literal("warp").requires(PartyUtils.partyLeader))

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(literal("p").redirect(commandNode))
    }
}
