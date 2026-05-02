package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand({"msgcancel", "msgtoggle"})
@Permission("essentials.command.msgcancel")
@Description("Unterbinde das empfangen privater Nachrichten")
public class MsgCancelCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();
    private final JavaPlugin schedulerExecutor = EssentialsPlugin.instance();

    @Execute
    public CompletableFuture<Void> cancelMsg(Player sender) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(schedulerExecutor, () -> {
            boolean disabledCurrently = chatSystem.hasDisabledPrivateMessages(sender.getUniqueId());
            chatSystem.setPrivateMessagesDisabled(sender.getUniqueId(), !disabledCurrently);
            if (disabledCurrently) {
                sender.sendMessage(Component.translatable("essentials.chat.msgcancel-disabled"));
            } else {
                sender.sendMessage(Component.translatable("essentials.chat.msgcancel-enabled"));
            }
            future.complete(null);
        });
        return future;
    }
}
