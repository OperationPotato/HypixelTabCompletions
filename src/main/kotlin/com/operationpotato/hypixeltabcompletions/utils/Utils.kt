package com.operationpotato.hypixeltabcompletions.utils

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import java.util.UUID
import java.util.function.Predicate

object Utils {
    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val uuidUsernameMap = Object2ObjectOpenHashMap<UUID, String>()

    fun isOnHypixel(): Boolean {
        val networkHandler: ClientPlayNetworkHandler = client.networkHandler ?: return false
        val brand = networkHandler.brand ?: return false

        return brand.contains("Hypixel")
    }

    val onHypixel = Predicate<FabricClientCommandSource> {
        isOnHypixel()
    }

    private fun fetchNameFromUUID(playerUUID: UUID): String {
        val world = client.world
        if (world != null) {
            val entity = world.getEntity(playerUUID)
            if (entity != null) return entity.name.string
        }

        // May need to be replaced with something else in the future...
        val profileResult = client.sessionService.fetchProfile(playerUUID, false)
        if (profileResult == null) return ""
        return profileResult.profile.name
    }

    fun getNameFromUUID(playerUUID: UUID): String {
        if (uuidUsernameMap.contains(playerUUID)) {
            return uuidUsernameMap.get(playerUUID) ?: ""
        }

        val username = fetchNameFromUUID(playerUUID)
        uuidUsernameMap.put(playerUUID, username)
        return username
    }
}
