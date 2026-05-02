package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.user.back.BackManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("back")
@Description("Nach dem Tot zurück")
@Permission(BackCommand.PERMISSION)
public class BackCommand {

    public static final String PERMISSION = "essentials.command.back";

    @Execute
    public void teleportBack(Player source) {
        if (!BackManager.getInstance().hasBackLocation(source.getUniqueId())) {
            source.sendMessage(Component.translatable("essentials.back.no-location"));
        }
        BackManager.getInstance().teleportBack(source);
    }
}
