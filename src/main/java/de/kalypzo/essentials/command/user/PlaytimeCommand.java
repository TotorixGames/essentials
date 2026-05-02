package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.user.EssentialsOfflineUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RootCommand("playtime")
public class PlaytimeCommand {
    private final PluginEnvironment environment = EssentialsPlugin.environment();

    @Execute
    public CompletableFuture<Void> showPlaytime(BukkitCommandSource source, @Optional EssentialsOfflineUser player) {
        if (player != null) {
            Duration duration = player.getPlayTime();
            String humanReadable = DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss", true);
            source.origin().sendMessage(Component.translatable("essentials.playtime.other",
                    Argument.component("target", Component.text(player.getName())),
                    Argument.component("playtime", Component.text(humanReadable))
            ));
            return CompletableFuture.completedFuture(null);
        }
        if (source.isConsole()) {
            source.origin().sendMessage(Component.translatable("essentials.command.player-only"));
            return CompletableFuture.completedFuture(null);
        }
        Player playerSender = source.asPlayer();
        return environment.getUser(playerSender.getUniqueId()).thenAccept(user -> {
            Duration duration = user.orElseThrow().getPlayTime();
            String humanReadable = DurationFormatUtils.formatDuration(duration.toMillis(), "HH:mm:ss", true);
            playerSender.sendMessage(Component.translatable("essentials.playtime.own",
                    Argument.component("playtime", Component.text(humanReadable))
            ));
        });
    }
}
