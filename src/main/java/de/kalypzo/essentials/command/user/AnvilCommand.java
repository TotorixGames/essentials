package de.kalypzo.essentials.command.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("anvil")
@Description("Opens anvil")
@Permission("essentials.command.anvil")
public class AnvilCommand {

    @Execute
    public void anvil(Player source) {
        MenuType.ANVIL.builder().build(source).open();
    }
}
