package de.kalypzo.essentials.command.user;

import com.destroystokyo.paper.profile.PlayerProfile;
import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.user.cooldown.RedisCooldownManager;
import de.kalypzo.essentials.util.MainThreadUtil;
import de.kalypzo.essentials.util.PermissionsRange;
import de.kalypzo.essentials.util.TagResolvers;
import de.kalypzo.essentials.util.Text;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import studio.mevera.imperat.annotations.types.Description;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RootCommand({"head", "skull"})
public class HeadCommand {

    private static class LAZY {
        private static final RedisCooldownManager INSTANCE = new RedisCooldownManager(EssentialsPlugin.instance().getRedis().connect(), "head");
    }

    @Execute
    @Description("Gibt dir deinen eigenen Kopf")
    @Permission("essentials.command.head.self")
    public CompletableFuture<Void> giveOwnHead(Player source) {
        return checkCooldown(source).thenCompose(allowed -> {
            if (allowed) {
                return giveHeadInternal(source, source);
            }
            return CompletableFuture.completedFuture(null);
        });
    }

    @Execute
    @Description("Gibt dir den Kopf eines Spielers")
    @Permission("essentials.command.head.other")
    public CompletableFuture<Void> giveHead(Player source, OfflinePlayer player) {
        return checkCooldown(source).thenCompose(allowed -> {
            if (allowed) {
                return giveHeadInternal(source, player);
            }
            return CompletableFuture.completedFuture(null);
        });
    }

    private CompletableFuture<Boolean> checkCooldown(Player sender) {
        if (!sender.hasPermission("essentials.cooldown.head.bypass")) {
            return CompletableFuture.supplyAsync(() -> {
                Duration duration = LAZY.INSTANCE.getCooldown(sender.getUniqueId());
                log.info("HeadCooldown check resulted in {} | senderName={} | sender={}", duration, sender.getName(), sender.getUniqueId());
                if (duration != null) {
                    sender.sendMessage(Text.deserialize("<prefix> <p>Du kannst in <duration> einen weiteren Kopf holen!",
                            net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.component("duration", Text.durationComponent(duration))));
                    return false;
                }
                int cooldownDays = PermissionsRange.min(sender, "essentials.cooldown.head");
                if (cooldownDays == -1) {
                    EssentialsPlugin.instance().getComponentLogger().warn("Player {}({}) has no cooldown permission configured and does not have a bypass. Using 1 DAY", sender.getUniqueId(), sender.getName());
                    cooldownDays = 1;
                }
                LAZY.INSTANCE.setCooldown(sender.getUniqueId(), Duration.ofDays(cooldownDays));
                return true;
            }, EssentialsPlugin.getExecutorService());
        }
        return CompletableFuture.completedFuture(true);
    }

    private CompletableFuture<Void> giveHeadInternal(Player sender, OfflinePlayer player) {
        PlayerProfile profile = player.getPlayerProfile();
        sender.sendActionBar(Component.text("Lade Kopf-Daten...", Text.getTextColor()));
        return profile.update().thenAcceptAsync(completedProfile -> {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            String name = player.getName();
            meta.setPlayerProfile(completedProfile);
            meta.customName(Component.text(name != null ? name : "?", Text.getHighlightColor()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            playerHead.setItemMeta(meta);
            sender.getInventory().addItem(playerHead);
            if (sender.getUniqueId().equals(player.getUniqueId())) {
                sender.sendMessage(Text.deserialize("<prefix> <p>Du hast dir deinen Kopf geholt!"));
            } else {
                sender.sendMessage(Text.deserialize("<prefix> <p>Du hast dir den Kopf von <hl><player></hl> geholt", TagResolvers.player(player)));
            }
        }, MainThreadUtil.createExecutor(sender)).exceptionally(ex -> {
            log.error("Player {}({}) could not be gathered!", player.getName(), player.getUniqueId(), ex);
            throw new RuntimeException(ex);
        });
    }
}
