package de.kalypzo.essentials.command.admin;

import de.kalypzo.essentials.util.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;
import studio.mevera.imperat.annotations.types.Switch;

import java.util.HashSet;
import java.util.Set;

@RootCommand("sendPurchaseNotification")
@Permission("essentials.admin.sendpurchasenotification")
public class SendMessage {
    private final Set<String> transactions = new HashSet<>();

    @Execute
    public void sendPurchaseNotification(BukkitCommandSource source, Player receiver, String transactionId, @Switch("announce") boolean announce) {
        if (transactions.contains(transactionId)) {
            source.origin().sendMessage(Text.deserialize("<ex>Kaufbenachrichtung wurde bereits gesendet!"));
            return;
        }
        receiver.playSound(receiver, Sound.ITEM_TOTEM_USE, 1f, 0.4f);
        receiver.playSound(receiver, Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.4f);
        receiver.showTitle(Title.title(Text.deserialize("<green>Danke für deine Unterstützung"), Text.deserialize("<p>Deine Benefits sind auf dem Weg...")));
        source.origin().sendActionBar(Component.text("delivered", NamedTextColor.GREEN));
        transactions.add(transactionId);
    }
}
