package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.home.Home;
import de.kalypzo.essentials.user.home.HomeManager;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.context.SuggestionContext;
import studio.mevera.imperat.exception.CommandException;
import studio.mevera.imperat.providers.SuggestionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeType extends ArgumentType<BukkitCommandSource, Home> {

    @Override
    public Home parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        if (ctx.source().isConsole()) {
            throw ComponentException.translatable("essentials.command.player-only");
        }
        return HomeManager.getInstance()
                .getHome(ctx.source().asPlayer().getUniqueId(), input)
                .join()
                .orElseThrow(() -> ComponentException.translatable("essentials.home.not-found", input));
    }

    @Override
    public SuggestionProvider<BukkitCommandSource> getSuggestionProvider() {
        return new SuggestionProvider<>() {
            @Override
            public List<String> provide(SuggestionContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg) {
                if (ctx.source().isConsole()) return Collections.emptyList();
                List<Home> homes = HomeManager.getInstance()
                        .getHomes(ctx.source().asPlayer().getUniqueId())
                        .join();
                List<String> names = new ArrayList<>(homes.size());
                for (Home h : homes) names.add(h.name());
                return names;
            }
        };
    }
}
