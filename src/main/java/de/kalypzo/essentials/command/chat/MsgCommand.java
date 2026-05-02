package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import de.kalypzo.essentials.chat.PrivateMessageResult;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Greedy;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("msg")
@Permission("essentials.command.msg")
public class MsgCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();
    private final JavaPlugin schedulerExecutor = EssentialsPlugin.instance();

    @Execute
    public CompletableFuture<Void> msg(Player sender, EssentialsUser receiver, @Greedy String message) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(schedulerExecutor, () -> {
            try {
                if (receiver.getUniqueId().equals(sender.getUniqueId())) {
                    throw ComponentException.translatable("essentials.chat.no-self-msg");
                }
                handlePrivateMessageResult(chatSystem.sendPrivateMessage(sender, receiver, message), future, receiver.getName());
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    static void handlePrivateMessageResult(PrivateMessageResult result, CompletableFuture<Void> future, @Nullable String receiverName) {
        switch (result) {
            case SUCCESS -> future.complete(null);
            case RECEIVER_DISABLED_PRIVATE_MESSAGES ->
                    future.completeExceptionally(ComponentException.translatable("essentials.chat.msg-disabled",
                            Argument.component("player", Component.text(receiverName))));
            case RECEIVER_IS_OFFLINE ->
                    future.completeExceptionally(ComponentException.translatable("essentials.user.offline"));
            case SENDER_MUST_NOT_BE_RECEIVER ->
                    future.completeExceptionally(ComponentException.translatable("essentials.chat.no-self-msg"));
        }
    }
}
