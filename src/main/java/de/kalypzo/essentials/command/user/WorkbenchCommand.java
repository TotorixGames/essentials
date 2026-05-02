package de.kalypzo.essentials.command.user;

import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand({"workbench", "wb", "craft"})
@Description("Öffne die Werkbank")
@Permission("essentials.command.workbench")
public class WorkbenchCommand {

    @Execute
    public void workbench(Player source) {
        MenuType.CRAFTING.builder().build(source).open();
    }
}
