package com.operationpotato.hypixeltabcompletions

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import com.operationpotato.hypixeltabcompletions.commands.Friend
import com.operationpotato.hypixeltabcompletions.commands.Guild
import com.operationpotato.hypixeltabcompletions.commands.Party
import com.operationpotato.hypixeltabcompletions.utils.Utils
import net.azureaaron.hmapi.events.HypixelPacketEvents
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import org.slf4j.Logger

object HypixelTabCompletions : ClientModInitializer {
    val logger: Logger = LogUtils.getLogger()

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
