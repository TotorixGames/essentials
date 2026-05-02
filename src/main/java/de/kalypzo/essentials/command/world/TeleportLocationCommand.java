package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.world.NetworkPosition;
import de.kalypzo.essentials.world.TeleportExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand({"teleportl", "tpl"})
@Permission("essentials.command.teleport")
@Description("Teleportiert dich zu einem Ort")
public class TeleportLocationCommand {
    private final PluginEnvironment environment = EssentialsPlugin.environment();

    @Execute
    public void teleport(Player source, double x, double y, double z, @Optional String world, @Optional String server) {
        if (world == null) {
            world = source.getWorld().getName();
        }
        if (server == null) {
            server = environment.getServerName();
        }
        if (server.equals(environment.getServerName()) && Bukkit.getWorld(world) == null) {
            source.sendMessage(Component.translatable("essentials.teleport.world-not-found",
                    Argument.component("world", Component.text(world))));
            return;
        }
        var pos = new NetworkPosition(server, world, x, y, z, source.getLocation().getYaw(), source.getLocation().getPitch());
        TeleportExecutor.getInstance().teleportPlayerToPosition(source.getUniqueId(), pos);
        source.playSound(source.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1.2f);
    }
}
