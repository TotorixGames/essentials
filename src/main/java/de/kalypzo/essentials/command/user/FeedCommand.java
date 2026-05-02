package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.user.cooldown.RedisCooldownManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.time.Duration;

@RootCommand("feed")
@Description("Fülle deinen Hunger")
@Permission("essentials.command.feed")
public class FeedCommand {

    public FeedCommand() {
        Permissions.registerAll();
    }

    @Execute
    public void feed(Player source) {
        Duration remaining = FeedCooldown.INSTANCE.getCooldown(source.getUniqueId());
        if (remaining != null) {
            source.sendMessage(Component.translatable("essentials.feed.cooldown", cooldownArgument(remaining)));
            return;
        }
        if (!source.hasPermission(Permissions.FEED_BYPASS.permission)) {
            Duration cooldown;
            if (source.hasPermission(Permissions.FEED_15.permission)) {
                cooldown = Duration.ofMinutes(15);
            } else if (source.hasPermission(Permissions.FEED_30.permission)) {
                cooldown = Duration.ofMinutes(30);
            } else if (source.hasPermission(Permissions.FEED_60.permission)) {
                cooldown = Duration.ofMinutes(60);
            } else {
                EssentialsPlugin.instance().getComponentLogger().warn("Player {} tried to feed without any cooldown-permission.", source.getName());
                source.sendMessage(Component.translatable("essentials.cooldown.unsupported-permission"));
                return;
            }
            FeedCooldown.INSTANCE.setCooldown(source.getUniqueId(), cooldown);
        }
        source.setFoodLevel(20);
        source.playSound(source, Sound.ENTITY_PLAYER_BURP, 1, 1.2f);
        source.sendMessage(Component.translatable("essentials.feed.success"));
    }

    private enum Permissions {
        FEED_BYPASS("essentials.cooldown.feed.bypass", "Kann /feed ohne Cooldown benutzen"),
        HEAL_BYPASS("essentials.cooldown.heal.bypass", "Kann /heal ohne Cooldown benutzen"),
        FEED_15("essentials.cooldown.feed.cooldown-15", "Alle 15 Minuten /feed"),
        FEED_30("essentials.cooldown.feed.cooldown-30", "Alle 30 Minuten /feed"),
        FEED_60("essentials.cooldown.feed.cooldown-60", "Alle 60 Minuten /feed"),
        HEAL_60("essentials.cooldown.heal.cooldown-60", "Alle 60 Minuten /heal"),
        HEAL_30("essentials.cooldown.heal.cooldown-30", "Alle 30 Minuten /heal");

        public final String permission;
        public final String description;

        Permissions(String permission, String description) {
            this.permission = permission;
            this.description = description;
        }

        public static void registerAll() {
            for (Permissions perm : Permissions.values()) {
                Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission(perm.permission, perm.description));
            }
        }
    }

    private ComponentLike cooldownArgument(Duration remaining) {
        long delta = Math.max(remaining.toMillis(), 0);
        return Argument.component("cooldown", Component.text(DurationFormatUtils.formatDuration(delta, "mm'm' ss's'")));
    }

    private static final class FeedCooldown {
        private static final RedisCooldownManager INSTANCE = new RedisCooldownManager(EssentialsPlugin.instance().getRedis().connect(), "feed");
    }
}
