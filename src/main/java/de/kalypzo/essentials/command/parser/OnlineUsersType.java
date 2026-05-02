package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.OnlineUsers;
import org.bukkit.Bukkit;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.exception.CommandException;

import java.util.List;

public class OnlineUsersType extends ArgumentType<BukkitCommandSource, OnlineUsers> {
    private static final String WILDCARD_ALL = "*";
    private static final String WILDCARD_ALL_ALT = "@a";
    private static final String WILDCARD_LOCAL = "@local";

    private final PluginEnvironment environment;

    public OnlineUsersType(PluginEnvironment environment) {
        this.environment = environment;
        addStaticSuggestions(WILDCARD_ALL, WILDCARD_ALL_ALT, WILDCARD_LOCAL);
    }

    @Override
    public OnlineUsers parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        if (input.equals(WILDCARD_ALL) || input.equals(WILDCARD_ALL_ALT)) {
            return new OnlineUsers(environment.getUsers().join(), true);
        }
        if (input.equals(WILDCARD_LOCAL)) {
            List<EssentialsUser> localUsers = Bukkit.getOnlinePlayers().stream()
                    .map(environment::adaptLocalPlayer)
                    .toList();
            return new OnlineUsers(localUsers, true);
        }
        throw ComponentException.translatable("essentials.players.invalid-selector", input);
    }
}
