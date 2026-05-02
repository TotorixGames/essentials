package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.OnlineUsers;
import org.bukkit.Bukkit;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class OnlineUsersParser implements ArgumentParser.FutureArgumentParser<Source, OnlineUsers>, SuggestionProvider<Source> {

    private static final String WILDCARD_ALL = "*";
    private static final String WILDCARD_ALL_ALT = "@a";
    private static final String WILDCARD_LOCAL = "@local";

    private final PluginEnvironment environment;

    public OnlineUsersParser(PluginEnvironment environment) {
        this.environment = environment;
    }

    public static ParserDescriptor<Source, OnlineUsers> descriptor(PluginEnvironment environment) {
        return ParserDescriptor.parserDescriptor(new OnlineUsersParser(environment), OnlineUsers.class);
    }

    @Override
    public CompletableFuture<ArgumentParseResult<OnlineUsers>> parseFuture(CommandContext<Source> context, CommandInput input) {
        String token = input.peekString();

        if (token.equals(WILDCARD_ALL) || token.equals(WILDCARD_ALL_ALT)) {
            return environment.getUsers().thenApply(users -> {
                input.readString();
                return ArgumentParseResult.success(new OnlineUsers(users, true));
            });
        }

        if (token.equals(WILDCARD_LOCAL)) {
            List<EssentialsUser> localUsers = Bukkit.getOnlinePlayers().stream()
                    .map(environment::adaptLocalPlayer)
                    .toList();
            input.readString();
            return CompletableFuture.completedFuture(ArgumentParseResult.success(new OnlineUsers(localUsers, true)));
        }

        return CompletableFuture.completedFuture(
                ArgumentParseResult.failure(ComponentException.translatable("essentials.players.invalid-selector", token))
        );
    }

    @Override
    public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(CommandContext<Source> context, CommandInput input) {
        return CompletableFuture.completedFuture(List.of(
                Suggestion.suggestion(WILDCARD_ALL),
                Suggestion.suggestion(WILDCARD_ALL_ALT),
                Suggestion.suggestion(WILDCARD_LOCAL)
        ));
    }
}
