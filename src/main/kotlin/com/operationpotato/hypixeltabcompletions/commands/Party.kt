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
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.commands.arguments.EntityArgument

object Party {
    private val playerArgument: RequiredArgumentBuilder<FabricClientCommandSource, EntitySelector> =
        argument("player", EntityArgument.player())

    private val partyMemberSuggestion: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("member", StringArgumentType.string()).suggests { ctx, builder ->
            PartyUtils.requestPartyInfo()
            SharedSuggestionProvider.suggest(PartyUtils.partyMembers, builder)
        }

    val messageArgument: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("message", StringArgumentType.greedyString()).suggests { ctx, builder ->
            PartyUtils.requestPartyInfo()
            SharedSuggestionProvider.suggest(PartyUtils.partyMembers, builder)
        }

    private val allInviteSettingLiteral: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("allinvite")

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("party")
            .requires(Utils.onHypixel)
            .then(literal("accept").then(playerArgument))
            .then(literal("invite").then(playerArgument).requires { ctx -> PartyUtils.partyModerator.test(ctx) || !PartyUtils.isInParty})

            .then(literal("demote").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))
            .then(literal("kick").then(partyMemberSuggestion).requires(PartyUtils.partyModerator))
            .then(literal("promote").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))
            .then(literal("transfer").then(partyMemberSuggestion).requires(PartyUtils.partyLeader))

            .then(literal("chat").then(messageArgument).requires(PartyUtils.inParty))

            .then(literal("settings").requires(PartyUtils.partyLeader).then(allInviteSettingLiteral))

            .then(literal("disband").requires(PartyUtils.partyLeader))
            .then(literal("kickoffline").requires(PartyUtils.partyLeader))
            .then(literal("leave").requires(PartyUtils.inParty))
            .then(literal("list").requires(PartyUtils.inParty))
            .then(literal("mute").requires(PartyUtils.partyModerator))
            .then(literal("poll").requires(PartyUtils.MVPPlusPlusPerks))
            .then(literal("private").requires(PartyUtils.MVPPlusPlusPerks))
            .then(literal("warp").requires(PartyUtils.partyLeader))

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    // Because /p is also registered as a server command, we have to do it like above
    val aliasRawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("p").redirect(commandNode)

    @JvmStatic
    val aliasCommandNode: LiteralCommandNode<FabricClientCommandSource> = aliasRawCommandNode.build()

    fun registerAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(rawCommandNode)
        dispatcher.register(aliasRawCommandNode)
    }
}
