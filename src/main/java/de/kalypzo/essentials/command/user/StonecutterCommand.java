package de.kalypzo.essentials.command.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("stonecutter")
@Description("Opens stone cutter")
@Permission("essentials.command.stonecutter")
public class StonecutterCommand {

    @Execute
    public void stoneCutter(Player source) {
        MenuType.STONECUTTER.builder().build(source).open();
    }
}
