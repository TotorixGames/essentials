package de.kalypzo.essentials.command.plot;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.rce.RemoteCommandCall;
import de.kalypzo.essentials.user.EssentialsOfflineUser;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.PathwayCommand;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Allows running plot-squared commands on a different server.
 * Registered conditionally if Plot-Squared is not present and proxying is enabled in the config.
 */
public class ProxiedPlotCommand {
    private final String serverName;
    private final PluginEnvironment environment;

    public ProxiedPlotCommand(String serverName) {
        this.serverName = serverName;
        this.environment = EssentialsPlugin.environment();
    }

    @PathwayCommand({"plot|p a", "plot|p auto"})
    public CompletableFuture<Void> proxy(Player source) {
        return proxyCommand(source.getUniqueId(), "plot auto", source);
    }

    @PathwayCommand({"plot|p home [plot]", "plot|p h [plot]"})
    public CompletableFuture<Void> proxyHome(Player source, @Optional Integer plot) {
        String command = "plot home" + (plot != null ? " " + plot : "");
        return proxyCommand(source.getUniqueId(), command, source);
    }

    @PathwayCommand({"plot|p visit <player> [plot]", "plot|p v <player> [plot]"})
    public CompletableFuture<Void> proxyVisit(Player source, EssentialsOfflineUser player, @Optional Integer plot) {
        String command = "plot visit " + player.getName() + (plot != null ? " " + plot : "");
        return proxyCommand(source.getUniqueId(), command, source);
    }

    private CompletableFuture<Void> proxyCommand(UUID playerId, String command, CommandSender sender) {
        return environment.connectPlayerToServer(playerId, serverName).thenAccept(success ->
                RemoteCommandCall.player(playerId, serverName, command).executeNow());
    }
}
