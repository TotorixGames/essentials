package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import it.einjojo.playerapi.PlayerApiProvider;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.context.SuggestionContext;
import studio.mevera.imperat.exception.CommandException;
import studio.mevera.imperat.providers.SuggestionProvider;

import java.util.List;
import java.util.UUID;

public class EssentialsUserType extends ArgumentType<BukkitCommandSource, EssentialsUser> {
    private static final int UUID_LENGTH = 36;
    private final PluginEnvironment environment;

    public EssentialsUserType(PluginEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public EssentialsUser parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        if (input.length() == UUID_LENGTH) {
            try {
                UUID uuid = UUID.fromString(input);
                return environment.getUser(uuid).join()
                        .orElseThrow(() -> ComponentException.translatable("essentials.player.offline", input));
            } catch (IllegalArgumentException e) {
                throw ComponentException.translatable("essentials.player.offline", input);
            }
        }
        return environment.getUserByName(input).join()
                .orElseThrow(() -> ComponentException.translatable("essentials.player.offline", input));
    }

    @Override
    public SuggestionProvider<BukkitCommandSource> getSuggestionProvider() {
        return new SuggestionProvider<>() {
            @Override
            public List<String> provide(SuggestionContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg) {
                return PlayerApiProvider.getInstance().getOnlinePlayerNames().join();
            }
        };
    }
}
