package de.kalypzo.essentials.command.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

@RootCommand("speed")
@Permission("essentials.command.speed")
@Description("Setzt deine Fluggeschwindigkeit oder Laufgeschwindigkeit")
public class SpeedCommand {

    @Execute
    public void speed(Player source, float speed) {
        float clampedInput = Math.max(0.0f, Math.min(10.0f, speed));
        float normalizedSpeed = clampedInput / 10.0f;
        if (source.isFlying()) {
            source.setFlySpeed(normalizedSpeed);
            source.sendMessage(Component.translatable("essentials.speed.flight.set",
                    Argument.component("speed", Component.text(String.format("%.1f", clampedInput)))
            ));
        } else {
            source.setWalkSpeed(normalizedSpeed);
            source.sendMessage(Component.translatable("essentials.speed.walk.set",
                    Argument.component("speed", Component.text(String.format("%.1f", clampedInput)))
            ));
        }
    }
}
