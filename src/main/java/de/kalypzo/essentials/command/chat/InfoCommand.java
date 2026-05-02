package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.util.Text;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("info")
public class InfoCommand {
    private final EssentialsPlugin plugin;

    public InfoCommand() {
        this.plugin = EssentialsPlugin.instance();
    }

    @Execute
    public void info(BukkitCommandSource source) {
        plugin.getConfig().getString("info-message", "info-message in config.yml not set.").lines().forEach(line -> {
            source.origin().sendMessage(Text.deserialize(line));
        });
    }
}
