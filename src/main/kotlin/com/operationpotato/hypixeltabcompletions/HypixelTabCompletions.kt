package com.operationpotato.hypixeltabcompletions

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import com.operationpotato.hypixeltabcompletions.commands.Friend
import com.operationpotato.hypixeltabcompletions.commands.Guild
import com.operationpotato.hypixeltabcompletions.commands.Party
import com.operationpotato.hypixeltabcompletions.commands.Reply
import com.operationpotato.hypixeltabcompletions.commands.Whisper
import com.operationpotato.hypixeltabcompletions.utils.PartyUtils
import net.azureaaron.hmapi.events.HypixelPacketEvents
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.CommandBuildContext
import org.slf4j.Logger

object HypixelTabCompletions : ClientModInitializer {
    val logger: Logger = LogUtils.getLogger()

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(::registerCommandAliases)

        HypixelPacketEvents.HELLO.register(PartyUtils::onHelloReceived)
        HypixelPacketEvents.PARTY_INFO.register(PartyUtils::onPartyInfoReceived)
        HypixelPacketEvents.PLAYER_INFO.register(PartyUtils::onPlayerInfoReceived)
    }

    fun registerCommandAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandBuildContext) {
        Friend.registerAliases(dispatcher)
        Guild.registerAliases(dispatcher)
        Party.registerAliases(dispatcher)
        Whisper.registerAliases(dispatcher)
        Reply.registerAliases(dispatcher)
    }
}
