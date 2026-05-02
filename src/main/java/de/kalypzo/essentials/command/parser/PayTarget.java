package de.kalypzo.essentials.command.parser;

import de.kalypzo.essentials.user.EssentialsOfflineUser;
import de.kalypzo.essentials.user.OnlineUsers;

public sealed interface PayTarget {
    record Multi(OnlineUsers users) implements PayTarget {}
    record Single(EssentialsOfflineUser user) implements PayTarget {}
}
