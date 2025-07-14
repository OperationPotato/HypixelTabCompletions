package com.operationpotato.hypixeltabcompletions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.operationpotato.hypixeltabcompletions.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Environment(EnvType.CLIENT)
@Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin<S> {
    // Small hack to remove child nodes with un-met requirements
    @WrapOperation(method = "getCompletionSuggestions(Lcom/mojang/brigadier/ParseResults;I)Ljava/util/concurrent/CompletableFuture;", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/tree/CommandNode;getChildren()Ljava/util/Collection;"), remap = false)
    public Collection<CommandNode<S>> removeRestrictedSubcommands(CommandNode<S> instance, Operation<Collection<CommandNode<S>>> original) {
        if (!Utils.INSTANCE.isOnHypixel()) return original.call(instance);
        String name = instance.getName();
        return switch (name) {
            case "party", "p" -> {
                Collection<CommandNode<S>> children = original.call(instance);
                children = children.stream().filter(child -> child.getRequirement().test(null)).toList();
                yield children;
            }
            default -> original.call(instance);
        };
    }
}
