package de.kalypzo.essentials.command.user;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand({"enderchest", "ec"})
public class EnderchestCommand {

    @Execute
    @Permission("essentials.command.enderchest")
    @Description("Öffnet deine Enderchest")
    public void openEnderchest(Player sender) {
        sender.openInventory(sender.getEnderChest());
        sender.playSound(sender, Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1.2f);
    }

    @Execute
    @Permission("essentials.command.enderchest.other")
    @Description("Öffnet die Enderchest eines Spielers")
    public void openOtherEnderchest(Player sender, Player player) {
        sender.openInventory(player.getEnderChest());
        sender.playSound(sender, Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1.2f);
    }
}
