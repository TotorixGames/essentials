package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.tpa.TpaManager;
import de.kalypzo.essentials.util.TagResolvers;
import de.kalypzo.essentials.util.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.concurrent.CompletableFuture;

@RootCommand("tpa")
public class TpaCommand {

    @Execute
    public CompletableFuture<Void> sendTpaRequest(Player sender, EssentialsUser player) {
        return TpaManager.getInstance().create(sender.getUniqueId(), player.getUniqueId())
                .thenAccept(request -> {
                    sender.sendMessage(Component.translatable("essentials.tpa.request-sent", Argument.tagResolver(player.playerTagResolver())));
                    Component tpaccept = Component.text("/tpaccept " + sender.getName()).clickEvent(ClickEvent.runCommand("/tpaccept " + sender.getName()));
                    player.sendMessage(Text.deserialize("<prefix> <p><hl><player></hl> möchte sich zu dir teleportieren.<newline>" +
                                    "<prefix> <p>Nutze: <hl><u><tpacmd>",
                            TagResolvers.player(sender),
                            Placeholder.component("tpacmd", tpaccept)));
                });
    }
}
