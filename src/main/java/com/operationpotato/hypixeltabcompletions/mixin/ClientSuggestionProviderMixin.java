package com.operationpotato.hypixeltabcompletions.mixin;

import com.operationpotato.hypixeltabcompletions.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;

@Environment(EnvType.CLIENT)
@Mixin(ClientSuggestionProvider.class)
public class ClientSuggestionProviderMixin {
    @Inject(method = "getOnlinePlayerNames", at = @At("TAIL"))
    public void getPlayerNames(CallbackInfoReturnable<Collection<String>> cir) {
        if (!Utils.INSTANCE.isOnHypixel()) return;

        Collection<String> playerNames = cir.getReturnValue();
        if (!(playerNames instanceof ArrayList<String> playerNamesList)) return;

        // Remove non-player names
        playerNamesList.removeIf((name) -> name.startsWith("!"));
    }
}
