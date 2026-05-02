package de.kalypzo.essentials.command.world;

import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("sethome")
@Permission("essentials.command.homes.set")
@Description("Erstellt ein Home auf der aktuellen Position")
public class SetHomeCommand {

    @Execute
    public CompletableFuture<Void> setHome(Player playerSource, String name) {
        return HomeCommand.doSetHome(playerSource, name);
    }
}
