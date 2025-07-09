package com.operationpotato.hypixeltabcompletions.utils

import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import java.util.UUID
import java.util.function.Predicate

object Utils {
    val client: MinecraftClient = MinecraftClient.getInstance()
    val partyMembers: ArrayList<String> = ArrayList()

    fun isOnHypixel(): Boolean {
        val networkHandler: ClientPlayNetworkHandler = client.networkHandler ?: return false
        val brand = networkHandler.brand ?: return false

        return brand.contains("Hypixel")
    }

    val onHypixel = Predicate<FabricClientCommandSource> {
        isOnHypixel()
    }

    fun getNameFromUUID(playerUUID: UUID): String {
        val world = client.world
        if (world != null) {
            val entity = world.getEntity(playerUUID)
            if (entity != null) return entity.name.string
        }

        val profileResult = client.sessionService.fetchProfile(playerUUID, false)
        if (profileResult == null) return ""
        return profileResult.profile.name
    }

    fun onPartyInfoReceived(packet: HypixelS2CPacket) {
        /*
        if (packet !is PartyInfoS2CPacket) return
        val memberUUIDs = packet.members ?: return

        partyMembers.clear()
        memberUUIDs.keys.forEach { uuid ->
            partyMembers.add(getNameFromUUID(uuid))
        }
        */
    }
}
