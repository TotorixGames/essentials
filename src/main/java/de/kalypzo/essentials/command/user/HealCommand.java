package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.command.Cooldowns;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.time.Duration;

@RootCommand("heal")
@Description("Heile dich")
@Permission("essentials.command.heal")
public class HealCommand {

    @Execute
    public void heal(Player source) {
        Duration remaining = Cooldowns.Heal.INSTANCE.getCooldown(source.getUniqueId());
        if (remaining != null) {
            source.sendMessage(Component.translatable("essentials.heal.cooldown", cooldownArgument(remaining)));
            return;
        }
        if (!source.hasPermission("essentials.cooldown.heal.bypass")) {
            Duration cooldown;
            if (source.hasPermission("essentials.cooldown.heal.cooldown-30")) {
                cooldown = Duration.ofMinutes(30);
            } else if (source.hasPermission("essentials.cooldown.heal.cooldown-60")) {
                cooldown = Duration.ofMinutes(60);
            } else {
                EssentialsPlugin.instance().getComponentLogger().warn("Player {} tried to heal without any cooldown-permission", source.getName());
                source.sendMessage(Component.translatable("essentials.heal.unsupported-permission"));
                return;
            }
            Cooldowns.Heal.INSTANCE.setCooldown(source.getUniqueId(), cooldown);
        }
        source.setHealth(20);
        source.setFoodLevel(20);
        source.playSound(source.getLocation(), Sound.ITEM_TOTEM_USE, 1, 1.2f);
        source.sendMessage(Component.translatable("essentials.heal.success"));
    }

    private ComponentLike cooldownArgument(Duration remaining) {
        long delta = Math.max(remaining.toMillis(), 0);
        return Argument.component("cooldown", Component.text(DurationFormatUtils.formatDuration(delta, "mm'm' ss's'")));
    }


}
