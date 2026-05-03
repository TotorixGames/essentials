package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.rce.RemoteCommandCall;
import de.kalypzo.essentials.user.EssentialsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand({"gamemode", "gm"})
@Permission("essentials.command.gamemode")
public class GameModeCommand {

    @Execute
    public void gameMode(BukkitCommandSource source, GameMode gamemode, @Optional EssentialsUser player) {
        if (player != null) {
            player.ifOnlineLocallyOrElse(bukkit -> {
                bukkit.setGameMode(gamemode);
            }, () -> {
                RemoteCommandCall.console(player.getServerName(), "gm " + gamemode.name() + " " + player.getName()).executeNow();
            });
            source.origin().sendMessage(Component.translatable("essentials.command.gamemode.other",
                    Argument.component("target", player),
                    Argument.component("gamemode", Component.text(gamemode.name()))
            ));
        } else {
            if (source.isConsole()) {
                source.origin().sendMessage(Component.translatable("essentials.command.player-only"));
                return;
            }
            Player playerSender = source.asPlayer();
            playerSender.setGameMode(gamemode);
            source.origin().sendMessage(Component.translatable("essentials.command.gamemode.self",
                    Argument.component("gamemode", Component.text(gamemode.name()))
            ));
        }
    }
}
