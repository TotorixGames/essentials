package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.user.UserSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.RootCommand;
import studio.mevera.imperat.annotations.types.SubCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand({"settings", "einstellungen"})
public class SettingsCommand {

    @SubCommand("togglePing")
    public CompletableFuture<Void> togglePingSounds(Player source) {
        return togglePing(source);
    }

    static CompletableFuture<Void> togglePing(Player source) {
        var settings = UserSettings.of(source.getUniqueId());
        boolean isDisabled = settings.disabledPingSound();
        return settings.disabledPingSound(!isDisabled).thenRun(() -> {
            if (isDisabled) {
                source.sendMessage(Component.translatable("essentials.settings.ping-active"));
                EssentialsPlugin.instance().getChatSystem().playPingSound(source);
            } else {
                source.sendMessage(Component.translatable("essentials.settings.ping-disabled"));
            }
        });
    }
}
