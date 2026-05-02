package de.kalypzo.essentials.command.admin;

import de.kalypzo.essentials.EssentialsPlugin;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Greedy;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("broadcast")
@Permission("essentials.command.broadcast")
@Description("Broadcasts a message to the server")
public class BroadcastCommand {

    @Execute
    public void broadcast(BukkitCommandSource source, @Greedy String message) {
        EssentialsPlugin.instance().getBroadcastManager().broadcastNetwork(message);
    }
}
