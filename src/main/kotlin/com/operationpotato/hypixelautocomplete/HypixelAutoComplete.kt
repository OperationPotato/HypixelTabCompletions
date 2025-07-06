package com.operationpotato.hypixelautocomplete

import com.mojang.brigadier.CommandDispatcher
import com.operationpotato.hypixelautocomplete.commands.Friend
import com.operationpotato.hypixelautocomplete.commands.Guild
import com.operationpotato.hypixelautocomplete.commands.Party
import com.operationpotato.hypixelautocomplete.utils.Utils
import net.azureaaron.hmapi.events.HypixelPacketEvents
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object HypixelAutoComplete : ClientModInitializer {
    val logger: Logger = LoggerFactory.getLogger("hypixel-auto-complete")

    override fun onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(::registerCommandAliases)
        HypixelPacketEvents.PARTY_INFO.register(Utils::onPartyInfoReceived)
    }

    fun registerCommandAliases(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        Friend.registerAliases(dispatcher)
        Guild.registerAliases(dispatcher)
        Party.registerAliases(dispatcher)
    }
}
