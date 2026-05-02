package de.kalypzo.essentials.command.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("loom")
@Description("Loom")
@Permission("essentials.command.loom")
public class LoomCommand {

    @Execute
    public void loom(Player source) {
        MenuType.LOOM.builder().build(source).open();
    }
}
