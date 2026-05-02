package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.chat.ChatMessage;
import de.kalypzo.essentials.util.Text;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.Execute;
import studio.mevera.imperat.annotations.types.Greedy;
import studio.mevera.imperat.annotations.types.Permission;
import studio.mevera.imperat.annotations.types.RootCommand;
import studio.mevera.imperat.annotations.types.SubCommand;

@RootCommand({"teamchat", "tc"})
public class TeamChatCommand {
    public static final String SCOPE = "essentials.teamchat.receive";
    public static Node TEAMCHAT_TOGGLED_NODE = Node.builder("essentialsmeta.teamchat.toggled").build();
    public static Node TEAMCHAT_SILENT_NODE = Node.builder("essentialsmeta.teamchat.silent").build();

    @Execute
    @Permission("essentials.command.teamchat")
    public void reply(Player sender, @Greedy String message) {
        Component output = Text.deserialize(PlaceholderAPI.setPlaceholders(sender, "<#a855f7>[<b>ᴛᴄ</b>] <white>%luckperms_prefix% %player_name%</white> <#f4f1de>→ <#a855f7><message>"),
                Placeholder.component("message", Component.text(message))
        );
        ChatMessage.createPermissionScoped(output, SCOPE).deliver();
    }

    @SubCommand("toggle")
    @Permission("essentials.command.teamchat.toggle")
    public void toggle(Player sender) {
        if (sender.hasPermission("essentialsmeta.teamchat.toggled")) {
            sender.sendMessage(Text.deserialize("<prefix> Alle Nachrichten werden nun in den Chat gesendet."));
            LuckPermsProvider.get().getUserManager().modifyUser(sender.getUniqueId(), user -> {
                user.data().remove(TEAMCHAT_TOGGLED_NODE);
            });
        } else {
            sender.sendMessage(Text.deserialize("<prefix> Alle Nachrichten werden nun im <#bdb2ff>TeamChat</<#bdb2ff> gesendet."));
            LuckPermsProvider.get().getUserManager().modifyUser(sender.getUniqueId(), user -> {
                user.data().add(TEAMCHAT_TOGGLED_NODE);
            });
        }
    }

    @SubCommand("silent")
    @Permission("essentials.command.silent")
    public void silent(Player sender) {
        if (sender.hasPermission("essentialsmeta.teamchat.silent")) {
            sender.sendMessage(Text.deserialize("<prefix> Du siehst wieder Team-Chat Nachrichten."));
            LuckPermsProvider.get().getUserManager().modifyUser(sender.getUniqueId(), user -> {
                user.data().remove(TEAMCHAT_SILENT_NODE);
            });
        } else {
            sender.sendMessage(Text.deserialize("<prefix> Du hast den Teamchat stummgeschaltet."));
            LuckPermsProvider.get().getUserManager().modifyUser(sender.getUniqueId(), user -> {
                user.data().add(TEAMCHAT_SILENT_NODE);
            });
        }
    }
}
