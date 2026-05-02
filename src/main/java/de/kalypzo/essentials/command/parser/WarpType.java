package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.world.warps.Warp;
import de.kalypzo.essentials.world.warps.WarpManager;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.context.SuggestionContext;
import studio.mevera.imperat.exception.CommandException;
import studio.mevera.imperat.providers.SuggestionProvider;

import java.util.List;

public class WarpType extends ArgumentType<BukkitCommandSource, Warp> {

    @Override
    public Warp parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        return WarpManager.getInstance().getWarp(input)
                .orElseThrow(() -> ComponentException.translatable("essentials.warp.not-found", input));
    }

    @Override
    public SuggestionProvider<BukkitCommandSource> getSuggestionProvider() {
        return new SuggestionProvider<>() {
            @Override
            public List<String> provide(SuggestionContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg) {
                return List.copyOf(WarpManager.getInstance().getWarpNames());
            }
        };
    }
}
