package de.kalypzo.essentials.command.user;

import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("togglePing")
public class TogglePingCommand {

    @Execute
    public CompletableFuture<Void> togglePing(Player source) {
        return SettingsCommand.togglePing(source);
    }
}
