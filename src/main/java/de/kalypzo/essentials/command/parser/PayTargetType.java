package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.OnlineUsers;
import it.einjojo.playerapi.PlayerApiProvider;
import org.bukkit.Bukkit;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.context.SuggestionContext;
import studio.mevera.imperat.exception.CommandException;
import studio.mevera.imperat.providers.SuggestionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PayTargetType extends ArgumentType<BukkitCommandSource, PayTarget> {
    private static final String WILDCARD_ALL = "*";
    private static final String WILDCARD_ALL_ALT = "@a";
    private static final String WILDCARD_LOCAL = "@local";

    private final PluginEnvironment environment;

    public PayTargetType(PluginEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public PayTarget parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        if (input.equals(WILDCARD_ALL) || input.equals(WILDCARD_ALL_ALT)) {
            return new PayTarget.Multi(new OnlineUsers(environment.getUsers().join(), true));
        }
        if (input.equals(WILDCARD_LOCAL)) {
            List<EssentialsUser> localUsers = Bukkit.getOnlinePlayers().stream()
                    .map(environment::adaptLocalPlayer)
                    .toList();
            return new PayTarget.Multi(new OnlineUsers(localUsers, true));
        }
        // try as offline player
        if (input.length() == 36) {
            try {
                UUID uuid = UUID.fromString(input);
                return new PayTarget.Single(environment.getOfflineUser(uuid).join()
                        .orElseThrow(() -> ComponentException.translatable("essentials.player.not-found", input)));
            } catch (IllegalArgumentException e) {
                throw ComponentException.translatable("essentials.player.not-found", input);
            }
        }
        return new PayTarget.Single(environment.getOfflineUserByName(input).join()
                .orElseThrow(() -> ComponentException.translatable("essentials.player.not-found", input)));
    }

    @Override
    public SuggestionProvider<BukkitCommandSource> getSuggestionProvider() {
        return new SuggestionProvider<>() {
            @Override
            public List<String> provide(SuggestionContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg) {
                List<String> suggestions = new ArrayList<>();
                suggestions.add(WILDCARD_ALL);
                suggestions.add(WILDCARD_ALL_ALT);
                suggestions.add(WILDCARD_LOCAL);
                suggestions.addAll(PlayerApiProvider.getInstance().getOnlinePlayerNames().join());
                return suggestions;
            }
        };
    }
}
