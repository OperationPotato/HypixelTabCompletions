package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.SharedSuggestionProvider

object Chat {
    // "a", "g", and "skyblock-coop" exist but are excluded to clean up the completions.
    private val channels =
        arrayListOf("all", "party", "guild", "officer", "skyblock", "coop")

    private val channelSuggestions: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("channel", StringArgumentType.string())
            .suggests { ctx, builder ->
                SharedSuggestionProvider.suggest(channels, builder)
            }

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("chat")
            .requires(Utils.onHypixel)
            .then(channelSuggestions)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()
}
