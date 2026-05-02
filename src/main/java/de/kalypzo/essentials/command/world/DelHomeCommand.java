package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.user.home.Home;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("delhome")
@Permission("essentials.command.homes.set")
@Description("Löscht ein Home")
public class DelHomeCommand {

    @Execute
    public CompletableFuture<Void> deleteHome(Player player, Home home) {
        return HomeCommand.doDeleteHome(player, home);
    }
}
