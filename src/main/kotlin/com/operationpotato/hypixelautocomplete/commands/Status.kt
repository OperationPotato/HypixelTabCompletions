package com.operationpotato.hypixelautocomplete.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandSource

object Status {
    private val options = listOf("online", "away", "busy", "offline")

    private val statusSuggestions: RequiredArgumentBuilder<FabricClientCommandSource, String> =
        argument("status", StringArgumentType.string()).suggests { ctx, builder ->
            CommandSource.suggestMatching(options, builder)
        }

    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("status")
            .then(statusSuggestions)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()
}
