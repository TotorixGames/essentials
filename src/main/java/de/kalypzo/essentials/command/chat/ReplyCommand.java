package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Greedy;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand({"r", "reply"})
@Permission("essentials.command.reply")
public class ReplyCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();
    private final JavaPlugin schedulerExecutor = EssentialsPlugin.instance();

    @Execute
    public CompletableFuture<Void> reply(Player sender, @Greedy String message) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(schedulerExecutor, () -> {
            MsgCommand.handlePrivateMessageResult(chatSystem.replyToLastMessage(sender, message), future, null);
        });
        return future;
    }
}
