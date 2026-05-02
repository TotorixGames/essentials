package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.tpa.TpaManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("tpaccept")
public class TpaAcceptCommand {

    @Execute
    public CompletableFuture<Void> accept(Player source, EssentialsUser player) {
        if (player.getUniqueId().equals(source.getUniqueId())) {
            source.sendMessage(Component.translatable("essentials.tpa.not-self"));
            return CompletableFuture.completedFuture(null);
        }
        var requestOpt = TpaManager.getInstance().getRequest(player.getUniqueId(), source.getUniqueId());
        if (requestOpt.isEmpty()) {
            source.sendMessage(Component.translatable("essentials.tpa.no-request", Argument.tagResolver(player.playerTagResolver())));
            return CompletableFuture.completedFuture(null);
        }
        return requestOpt.get().fulfill()
                .thenAccept(_void -> source.sendMessage(Component.translatable("essentials.tpa.accept", Argument.tagResolver(player.playerTagResolver()))));
    }
}
