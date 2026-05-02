package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.world.warps.Warp;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("warp")
public class WarpCommand {
    public static final String SET_PERMISSION = "essentials.command.warp.set";
    public static final String DELETE_PERMISSION = "essentials.command.warp.delete";


    @Execute
    public void warp(Player player, Warp warp) {
        warp.teleport(player, Warp.Reason.COMMAND);
    }
}
