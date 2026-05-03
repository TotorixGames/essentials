package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsOfflineUser;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.exception.CommandException;
import studio.mevera.imperat.providers.SuggestionProvider;

import java.util.UUID;

public class EssentialsOfflineUserType extends ArgumentType<BukkitCommandSource, EssentialsOfflineUser> {
    private static final int UUID_LENGTH = 36;
    private final PluginEnvironment environment;

    public EssentialsOfflineUserType(PluginEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public EssentialsOfflineUser parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        if (input.length() == UUID_LENGTH) {
            try {
                UUID uuid = UUID.fromString(input);
                return environment.getOfflineUser(uuid).join()
                        .orElseThrow(() -> ComponentException.translatable("essentials.player.not-found", input));
            } catch (IllegalArgumentException e) {
                throw ComponentException.translatable("essentials.player.not-found", input);
            }
        }
        return environment.getOfflineUserByName(input).join()
                .orElseThrow(() -> ComponentException.translatable("essentials.player.not-found", input));
    }

    @Override
    public SuggestionProvider<BukkitCommandSource> getSuggestionProvider() {
        return (ctx, arg) -> {
            UUID playerUuid = ctx.source().isConsole() ? null : ctx.source().asPlayer().getUniqueId();
            return environment.suggestOfflinePlayerNames(ctx.getArgToComplete().value(), playerUuid).join();
        };
    }
}
