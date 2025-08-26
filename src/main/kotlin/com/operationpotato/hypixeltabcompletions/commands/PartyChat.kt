package com.operationpotato.hypixeltabcompletions.commands

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object PartyChat {
    private val rawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("pc")
            .requires(Utils.onHypixel)
            .then(Party.messageArgument)

    @JvmStatic
    val commandNode: LiteralCommandNode<FabricClientCommandSource> = rawCommandNode.build()

    private val aliasRawCommandNode: LiteralArgumentBuilder<FabricClientCommandSource> =
        literal("pchat")
            .redirect(commandNode)

    @JvmStatic
    val aliasCommandNode: LiteralCommandNode<FabricClientCommandSource> = aliasRawCommandNode.build()
}
