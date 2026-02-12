package com.operationpotato.hypixeltabcompletions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.operationpotato.hypixeltabcompletions.commands.*;
import com.operationpotato.hypixeltabcompletions.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.commands.SharedSuggestionProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.network.protocol.game.ClientboundCommandsPacket$NodeResolver")
public class CommandTreeMixin {
    @ModifyExpressionValue(method = "resolve", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$NodeResolver;resolve(I)Lcom/mojang/brigadier/tree/CommandNode;", ordinal = 1))
    public CommandNode<? extends SharedSuggestionProvider> modifyCommandSuggestions(CommandNode<SharedSuggestionProvider> original) {
        if (!Utils.INSTANCE.isOnHypixel() || !(original instanceof LiteralCommandNode<?> literal)) return original;
        String name = literal.getLiteral();

        return switch (name) {
            case "party" -> Party.getCommandNode();
            case "p" -> Party.getAliasCommandNode();
            case "pc" -> PartyChat.getCommandNode();
            case "pchat" -> PartyChat.getAliasCommandNode();
            case "guild" -> Guild.getCommandNode();
            case "friend" -> Friend.getCommandNode();
            case "fl" -> FriendsList.getCommandNode();
            case "chat" -> Chat.getCommandNode();
            case "status" -> Status.getCommandNode();
            case "r" -> Reply.getCommandNode();
            default -> original;
        };
    }
}
