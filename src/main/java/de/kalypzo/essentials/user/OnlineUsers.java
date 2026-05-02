package de.kalypzo.essentials.user;

import java.util.List;

public record OnlineUsers(List<EssentialsUser> users, boolean isWildcard) {
}
