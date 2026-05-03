package de.kalypzo.essentials.command.chat;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.chat.ChatSystem;
import de.kalypzo.essentials.chat.PrivateMessageResult;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.user.EssentialsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.entity.Player;
import studio.mevera.imperat.annotations.types.*;

@RootCommand("msg")
@Permission("essentials.command.msg")
public class MsgCommand {
    private final ChatSystem chatSystem = EssentialsPlugin.instance().getChatSystem();

    @Execute
    @Async
    public void msg(Player sender, EssentialsUser receiver, @Greedy String message) {
        if (receiver.getUniqueId().equals(sender.getUniqueId())) {
            throw ComponentException.translatable("essentials.chat.no-self-msg");
        }
        handlePrivateMessageResult(chatSystem.sendPrivateMessage(sender, receiver, message), receiver.getName());
    }

    static void handlePrivateMessageResult(PrivateMessageResult result, String receiverName) {
        switch (result) {
            case RECEIVER_DISABLED_PRIVATE_MESSAGES ->
                    throw ComponentException.translatable("essentials.chat.msg-disabled",
                            Argument.component("player", Component.text(receiverName)));
            case RECEIVER_IS_OFFLINE -> throw (ComponentException.translatable("essentials.user.offline"));
            case SENDER_MUST_NOT_BE_RECEIVER -> throw (ComponentException.translatable("essentials.chat.no-self-msg"));
        }
    }
}
