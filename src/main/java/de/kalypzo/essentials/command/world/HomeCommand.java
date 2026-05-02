package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.event.PlayerSetHomeEvent;
import de.kalypzo.essentials.exception.BadConfigurationException;
import de.kalypzo.essentials.gui.home.GuiHomes;
import de.kalypzo.essentials.user.home.Home;
import de.kalypzo.essentials.user.home.HomeManager;
import de.kalypzo.essentials.world.NetworkPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;
import studio.mevera.imperat.annotations.types.SubCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand({"home", "homes"})
public class HomeCommand {

    @Execute
    @Permission("essentials.command.homes")
    public void openHomeGui(Player playerSource) {
        try {
            new GuiHomes(playerSource, HomeManager.getInstance()).open();
        } catch (BadConfigurationException e) {
            EssentialsPlugin.instance().getSLF4JLogger().error("Failed to open home GUI for player {}", playerSource.getName(), e);
            playerSource.sendMessage(Component.translatable("essentials.homes.gui.config-error"));
        }
    }

    @Execute
    @Permission("essentials.command.homes.tp")
    @Description("Teleportiert den Spieler zu seinem Home")
    public void teleportHome(Player player, Home home) {
        player.sendMessage(Component.translatable("essentials.homes.teleport",
                Argument.component("name", Component.text(home.name()))));
        home.teleport(player);
    }

    @SubCommand("set")
    @Permission("essentials.command.homes.set")
    @Description("Erstellt ein Home auf der aktuellen Position")
    public CompletableFuture<Void> setHome(Player playerSource, String name) {
        return doSetHome(playerSource, name);
    }

    @SubCommand("delete")
    @Permission("essentials.command.homes.set")
    @Description("Löscht ein Home")
    public CompletableFuture<Void> deleteHome(Player player, Home home) {
        return doDeleteHome(player, home);
    }

    static CompletableFuture<Void> doSetHome(Player player, String name) {
        var event = new PlayerSetHomeEvent(player);
        if (!event.callEvent()) {
            return CompletableFuture.completedFuture(null);
        }
        final int maxHomes = HomeManager.getInstance().getMaxHomes(player);
        if (maxHomes < 1) {
            player.sendMessage(Component.translatable("essentials.homes.set.max-reached",
                    Argument.numeric("amount", maxHomes)));
            return CompletableFuture.completedFuture(null);
        }
        return HomeManager.getInstance().getHomes(player.getUniqueId()).thenCompose(homes ->
                HomeManager.getInstance().getHome(player.getUniqueId(), name).thenCompose(existing -> {
                    if (homes.size() >= maxHomes && existing.isEmpty()) {
                        player.sendMessage(Component.translatable("essentials.homes.set.max-reached",
                                Argument.numeric("amount", maxHomes)));
                        return CompletableFuture.completedFuture(null);
                    }
                    return HomeManager.getInstance().saveHome(new Home(
                                    player.getUniqueId(),
                                    name,
                                    NetworkPosition.createByLocation(player.getLocation())))
                            .thenAccept(_void -> player.sendMessage(Component.translatable("essentials.homes.set.success",
                                    Argument.component("name", Component.text(name)))));
                }));
    }

    static CompletableFuture<Void> doDeleteHome(Player player, Home home) {
        return HomeManager.getInstance().deleteHome(home).thenAccept(_void ->
                player.sendMessage(Component.translatable("essentials.homes.delete.success",
                        Argument.component("name", Component.text(home.name())))));
    }
}
