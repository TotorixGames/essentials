package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.world.warps.Warp;
import de.kalypzo.essentials.world.warps.WarpManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("farmwelt")
public class FarmweltCommand {

    @Execute
    public void farmwelt(Player source) {
        WarpManager.getInstance().getWarp("farmwelt").ifPresentOrElse(
                warp -> warp.teleport(source, Warp.Reason.COMMAND),
                () -> source.sendMessage(Component.translatable("essentials.warp.not-found", Component.text("farmwelt"))));
    }
}
