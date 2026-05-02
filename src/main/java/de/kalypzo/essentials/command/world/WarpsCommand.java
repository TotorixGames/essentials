package de.kalypzo.essentials.command.world;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.world.NetworkPosition;
import de.kalypzo.essentials.world.warps.Warp;
import de.kalypzo.essentials.world.warps.WarpManager;
import de.kalypzo.essentials.gui.warps.GuiWarps;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Flag;
import studio.mevera.imperat.annotations.types.Optional;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;
import studio.mevera.imperat.annotations.types.SubCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("warps")
public class WarpsCommand {

    @Execute
    public void list(Player source) {
        openGui(source);
    }

    @SubCommand("list")
    public void listSub(Player source) {
        openGui(source);
    }

    @SubCommand("reload")
    @Permission("essentials.command.warp.reload")
    public CompletableFuture<Void> reload(BukkitCommandSource source) {
        return WarpManager.getInstance().load().thenAccept(_void ->
                source.origin().sendMessage(Component.translatable("essentials.warp.reload")));
    }

    @SubCommand("set")
    @Permission(WarpCommand.SET_PERMISSION)
    public CompletableFuture<Void> save(Player source, String name,
            @Optional @Flag("permission") String permission,
            @Optional @Flag("displayName") String displayName) {
        Component component;
        if (displayName != null) {
            component = MiniMessage.miniMessage().deserialize(displayName).colorIfAbsent(NamedTextColor.AQUA);
        } else {
            component = Component.text(name).colorIfAbsent(NamedTextColor.AQUA);
        }
        var warp = new Warp(name, component, permission, NetworkPosition.createByLocation(source.getLocation()));
        return WarpManager.getInstance().saveWarp(warp).thenAccept(_void ->
                source.sendMessage(Component.translatable("essentials.warp.set", warp)));
    }

    @SubCommand("delete")
    @Permission(WarpCommand.DELETE_PERMISSION)
    public CompletableFuture<Void> delete(Player source, Warp warp) {
        return WarpManager.getInstance().deleteWarp(warp).thenAccept(_v ->
                source.sendMessage(Component.translatable("essentials.warp.deleted", warp)));
    }

    private void openGui(Player source) {
        try {
            new GuiWarps(source, WarpManager.getInstance(), EssentialsPlugin.instance().getWarpsConfig()).open();
        } catch (Exception e) {
            EssentialsPlugin.instance().getSLF4JLogger().error("Failed to open warps GUI for player {}: {}", source.getName(), e.getMessage());
            source.sendMessage(Component.translatable("essentials.warp.gui-load-failed"));
        }
    }
}
