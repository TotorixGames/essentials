package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import de.kalypzo.essentials.chat.PrivateMessageResult;
import de.kalypzo.essentials.user.EssentialsUser;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.*;

import java.util.Optional;
import java.util.UUID;

@RootCommand({"r", "reply"})
@Permission("essentials.command.reply")
public class ReplyCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();

    @Execute
    @Async
    public void reply(Player sender, @Greedy String message) {
        UUID lastMessageSender = chatSystem.getLastMessageSender(sender.getUniqueId());
        if (lastMessageSender == null) {
            MsgCommand.handlePrivateMessageResult(PrivateMessageResult.NO_REPLY_TARGET, null);
            return;
        }
        Optional<EssentialsUser> target = EssentialsPlugin.environment().getUser(lastMessageSender).join();
        if (target.isEmpty()) {
            MsgCommand.handlePrivateMessageResult(PrivateMessageResult.RECEIVER_IS_OFFLINE, null);
            return;
        }
        var result = chatSystem.sendPrivateMessage(sender, target.get(), message);
        MsgCommand.handlePrivateMessageResult(result, target.get().getName());
    }
}
