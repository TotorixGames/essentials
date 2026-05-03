package de.kalypzo.essentials.command.user;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.command.parser.PayTarget;
import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.exception.TransactionException;
import de.kalypzo.essentials.user.EssentialsOfflineUser;
import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.OnlineUsers;
import it.einjojo.economy.EconomyService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RootCommand("pay")
@Description("Überweise Geld an einen Spieler")
public class PayCommand {
    private final PluginEnvironment environment;
    private final EconomyService economyService;

    public PayCommand() {
        this.environment = EssentialsPlugin.environment();
        this.economyService = Objects.requireNonNull(Bukkit.getServicesManager().load(EconomyService.class), "EconomyService not found");
    }

    @Execute
    public CompletableFuture<Void> pay(Player sender, PayTarget players, int amount) {
        if (players instanceof PayTarget.Multi(OnlineUsers users)) {
            return payMulti(sender, users, amount);
        } else {
            return paySingle(sender, ((PayTarget.Single) players).user(), amount);
        }
    }

    private CompletableFuture<Void> payMulti(Player sender, OnlineUsers players, int amount) {
        if (amount < 1) {
            throw ComponentException.translatable("essentials.money.pay.amount-too-low");
        }
        UUID senderUuid = sender.getUniqueId();
        List<EssentialsUser> recipients = players.users().stream()
                .filter(u -> !u.getUuid().equals(senderUuid))
                .toList();
        if (recipients.isEmpty()) {
            throw ComponentException.translatable("essentials.money.pay.no-targets");
        }
        int total = amount * recipients.size();
        return economyService.withdraw(senderUuid, total, "pay to " + recipients.size() + " players")
                .thenCompose(result -> {
                    if (!result.isSuccess()) {
                        throw new TransactionException(result.status());
                    }
                    CompletableFuture<?>[] deposits = recipients.stream()
                            .map(user -> economyService.deposit(user.getUuid(), amount, "pay from " + sender.getName())
                                    .thenAccept(ignored -> notifyPaymentReceive(user.getUuid(), sender.displayName(), amount)))
                            .toArray(CompletableFuture[]::new);
                    return CompletableFuture.allOf(deposits)
                            .thenRun(() -> sender.sendMessage(Component.translatable(
                                    "essentials.money.pay.multi.sent",
                                    Argument.numeric("count", recipients.size()),
                                    Argument.numeric("amount", amount),
                                    Argument.numeric("total", total)
                            )));
                });
    }

    private CompletableFuture<Void> paySingle(Player sender, EssentialsOfflineUser player, int amount) {
        if (amount < 1) {
            throw ComponentException.translatable("essentials.money.pay.amount-too-low");
        }
        if (sender.getUniqueId().equals(player.getUniqueId())) {
            throw ComponentException.translatable("essentials.money.pay.no-self-pay");
        }
        return economyService.withdraw(sender.getUniqueId(), amount, "pay to " + player.getName())
                .thenCompose(result -> {
                    if (!result.isSuccess()) {
                        throw new TransactionException(result.status());
                    }
                    return economyService.deposit(player.getUniqueId(), amount, "pay from " + sender.getName())
                            .thenAccept(success -> {
                                sender.sendMessage(Component.translatable("essentials.money.pay.sent",
                                        Argument.component("target", Component.text(player.getName())),
                                        Argument.numeric("amount", amount)
                                ));
                                notifyPaymentReceive(player.getUniqueId(), sender.displayName(), amount);
                            });
                });
    }

    private void notifyPaymentReceive(UUID target, Component sender, int amount) {
        environment.getUser(target).thenAccept(optional ->
                optional.ifPresent(user -> user.sendMessage(Component.translatable("essentials.money.pay.received",
                        Argument.component("sender", sender),
                        Argument.numeric("amount", amount)
                ))));
    }
}
