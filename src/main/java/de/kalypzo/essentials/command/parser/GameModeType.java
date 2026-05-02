package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.exception.ComponentException;
import org.bukkit.GameMode;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.command.arguments.Argument;
import studio.mevera.imperat.command.arguments.type.ArgumentType;
import studio.mevera.imperat.context.CommandContext;
import studio.mevera.imperat.exception.CommandException;

public class GameModeType extends ArgumentType<BukkitCommandSource, GameMode> {

    public GameModeType() {
        addStaticSuggestions("0", "1", "2", "3", "s", "c", "a", "o", "survival", "creative", "adventure", "spectator");
    }

    @Override
    public GameMode parse(CommandContext<BukkitCommandSource> ctx, Argument<BukkitCommandSource> arg, String input) throws CommandException {
        return switch (input) {
            case "1", "c", "creative" -> GameMode.CREATIVE;
            case "2", "a", "adventure" -> GameMode.ADVENTURE;
            case "3", "o", "spectator" -> GameMode.SPECTATOR;
            case "0", "s", "survival" -> GameMode.SURVIVAL;
            default -> throw ComponentException.translatable("essentials.gamemode.invalid", input);
        };
    }
}
