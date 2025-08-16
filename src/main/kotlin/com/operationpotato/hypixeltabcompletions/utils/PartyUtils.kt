package com.operationpotato.hypixeltabcompletions.utils

import com.operationpotato.hypixeltabcompletions.utils.Utils.getNameFromUUID
import net.azureaaron.hmapi.data.party.PartyRole
import net.azureaaron.hmapi.data.rank.MonthlyPackageRank
import net.azureaaron.hmapi.data.rank.PlayerRank
import net.azureaaron.hmapi.network.HypixelNetworking
import net.azureaaron.hmapi.network.packet.s2c.HelloS2CPacket
import net.azureaaron.hmapi.network.packet.s2c.HypixelS2CPacket
import net.azureaaron.hmapi.network.packet.v1.s2c.PlayerInfoS2CPacket
import net.azureaaron.hmapi.network.packet.v2.s2c.PartyInfoS2CPacket
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate

object PartyUtils {
    private val client: MinecraftClient = MinecraftClient.getInstance()

    var isInParty = false
    private var isPartyLeader = false
    private var isPartyModerator = false
    private var hasMVPPlusPlusPerks = false
    private var partyLastUpdated: Long = 0

    val partyMembers: ArrayList<String> = ArrayList()

    val partyLeader = Predicate<FabricClientCommandSource> {
        requestPartyInfo()
        isPartyLeader
    }

    val partyModerator = Predicate<FabricClientCommandSource> {
        requestPartyInfo()
        isPartyModerator
    }

    val inParty = Predicate<FabricClientCommandSource> {
        requestPartyInfo()
        isInParty
    }

    val MVPPlusPlusPerks = Predicate<FabricClientCommandSource> {
        hasMVPPlusPlusPerks
    }

    fun onHelloReceived(packet: HypixelS2CPacket) {
        if (packet !is HelloS2CPacket) return
        requestPartyInfo(true)
        HypixelNetworking.sendPlayerInfoC2SPacket(1)
    }

    fun onPartyInfoReceived(packet: HypixelS2CPacket) {
        if (packet !is PartyInfoS2CPacket) return
        partyLastUpdated = System.currentTimeMillis()
        isInParty = packet.inParty

        if (!packet.inParty) {
            isPartyModerator = false
            isPartyLeader = false
        }
        partyMembers.clear()

        val memberUUIDs = packet.members ?: return // party members are null when not in a party.
        memberUUIDs.keys.forEach { uuid ->
            CompletableFuture.runAsync {
                val username = getNameFromUUID(uuid)
                client.execute { partyMembers.add(username) }
            }

            if (uuid == client.player?.uuid) {
                isPartyLeader = memberUUIDs[uuid] == PartyRole.LEADER
                isPartyModerator = isPartyLeader || memberUUIDs[uuid] == PartyRole.MODERATOR
            }
        }
    }

    fun onPlayerInfoReceived(packet: HypixelS2CPacket) {
        if (packet !is PlayerInfoS2CPacket) return
        hasMVPPlusPlusPerks = if (packet.playerRank == PlayerRank.NORMAL) {
            packet.monthlyPackageRank == MonthlyPackageRank.SUPERSTAR
        } else {
            // YouTube Rank and Staff
            true
        }
    }

    fun requestPartyInfo(force: Boolean = false) {
        if (!Utils.isOnHypixel()) return
        if (force || (System.currentTimeMillis() - partyLastUpdated > 10_000)) {
            // Update this here to prevent sending multiple requests while waiting for the previous request to come back
            partyLastUpdated = System.currentTimeMillis()
            HypixelNetworking.sendPartyInfoC2SPacket(2)
        }
    }
}
