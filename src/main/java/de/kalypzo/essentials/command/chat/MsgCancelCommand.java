package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.*;

@RootCommand({"msgcancel", "msgtoggle"})
@Permission("essentials.command.msgcancel")
@Description("Unterbinde das empfangen privater Nachrichten")
public class MsgCancelCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();

    @Execute
    @Async
    public void cancelMsg(Player sender) {
        boolean disabledCurrently = chatSystem.hasDisabledPrivateMessages(sender.getUniqueId());
        chatSystem.setPrivateMessagesDisabled(sender.getUniqueId(), !disabledCurrently);
        if (disabledCurrently) {
            sender.sendMessage(Component.translatable("essentials.chat.msgcancel-disabled"));
        } else {
            sender.sendMessage(Component.translatable("essentials.chat.msgcancel-enabled"));
        }
    }
}
