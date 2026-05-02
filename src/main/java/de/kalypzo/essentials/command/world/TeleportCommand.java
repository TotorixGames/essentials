package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.util.TranslationConstants;
import de.kalypzo.essentials.world.TeleportExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand({"teleport", "tp"})
@Description("Teleportiert dich zu einem Spieler oder teleportiert einen Spieler zu einem anderen")
@Permission("essentials.command.teleport")
public class TeleportCommand {

    @Execute
    public void teleport(BukkitCommandSource source, EssentialsUser target, @Optional EssentialsUser destination) {
        if (destination != null) {
            target.teleport(destination);
            source.origin().sendMessage(Component.translatable("essentials.teleport.other-success",
                    Argument.component("target", Component.text(target.getName())),
                    Argument.component("destination", Component.text(destination.getName()))
            ));
            return;
        }
        if (source.isConsole()) {
            source.origin().sendMessage(TranslationConstants.COMMAND_REQUIRES_PLAYER_EXECUTOR);
            return;
        }
        source.origin().sendMessage(Component.translatable("essentials.teleport.success",
                Argument.component("target", Component.text(target.getName()))
        ));
        TeleportExecutor.getInstance().teleportPlayerToPlayer(source.asPlayer().getUniqueId(), target.getUniqueId());
    }
}
