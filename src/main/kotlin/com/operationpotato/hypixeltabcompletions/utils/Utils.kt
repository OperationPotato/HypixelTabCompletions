package com.operationpotato.hypixeltabcompletions.utils

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import java.util.UUID
import java.util.function.Predicate

object Utils {
    private val client: Minecraft = Minecraft.getInstance()
    private val uuidUsernameMap = Object2ObjectOpenHashMap<UUID, String>()
    private val isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment

    fun isOnHypixel(): Boolean {
        val networkHandler: ClientPacketListener = client.connection ?: return false
        val brand = networkHandler.serverBrand() ?: return false

        return brand.contains("Hypixel")
    }

    val onHypixel = Predicate<FabricClientCommandSource> {
        isOnHypixel() || isDevelopmentEnvironment
    }

    private fun fetchNameFromUUID(playerUUID: UUID): String {
        val world = client.level
        if (world != null) {
            val entity = world.getEntity(playerUUID)
            if (entity != null) return entity.name.string
        }

        // May need to be replaced with something else in the future...
        val profileResult = client.services().sessionService.fetchProfile(playerUUID, false) ?: return ""
        return profileResult.profile.name
    }

    fun getNameFromUUID(playerUUID: UUID): String {
        return uuidUsernameMap.computeIfAbsent(playerUUID, ::fetchNameFromUUID)
    }
}
