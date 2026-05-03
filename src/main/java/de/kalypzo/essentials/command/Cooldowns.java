package de.kalypzo.essentials.command;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.user.cooldown.RedisCooldownManager;

public class Cooldowns {

    public static final class Heal {
        public static final RedisCooldownManager INSTANCE = new RedisCooldownManager(EssentialsPlugin.instance().getRedis().connect(), "heal");
    }

    public static final RedisCooldownManager FEED = new RedisCooldownManager(EssentialsPlugin.instance().getRedis().connect(), "feed");
    public static final RedisCooldownManager HEAD = new RedisCooldownManager(EssentialsPlugin.instance().getRedis().connect(), "head");
}
