package de.kalypzo.essentials.command;

import de.kalypzo.essentials.EssentialsPlugin;
import de.kalypzo.essentials.command.admin.BroadcastCommand;
import de.kalypzo.essentials.command.admin.DebugCommand;
import de.kalypzo.essentials.command.admin.LabyModDebugCommand;
import de.kalypzo.essentials.command.admin.SendMessage;
import de.kalypzo.essentials.command.chat.DiscordCommand;
import de.kalypzo.essentials.command.chat.InfoCommand;
import de.kalypzo.essentials.command.chat.MsgCancelCommand;
import de.kalypzo.essentials.command.chat.MsgCommand;
import de.kalypzo.essentials.command.chat.ReplyCommand;
import de.kalypzo.essentials.command.chat.ShopCommand;
import de.kalypzo.essentials.command.chat.TeamChatCommand;
import de.kalypzo.essentials.command.parser.EssentialsOfflineUserType;
import de.kalypzo.essentials.command.parser.EssentialsUserType;
import de.kalypzo.essentials.command.parser.GameModeType;
import de.kalypzo.essentials.command.parser.HomeType;
import de.kalypzo.essentials.command.parser.OnlineUsersType;
import de.kalypzo.essentials.command.parser.PayTarget;
import de.kalypzo.essentials.command.parser.PayTargetType;
import de.kalypzo.essentials.command.parser.WarpType;
import de.kalypzo.essentials.command.plot.ProxiedPlotCommand;
import de.kalypzo.essentials.command.user.AnvilCommand;
import de.kalypzo.essentials.command.user.BackCommand;
import de.kalypzo.essentials.command.user.BalanceCommand;
import de.kalypzo.essentials.command.user.EnderchestCommand;
import de.kalypzo.essentials.command.user.FeedCommand;
import de.kalypzo.essentials.command.user.FlyCommand;
import de.kalypzo.essentials.command.user.GameModeCommand;
import de.kalypzo.essentials.command.user.HeadCommand;
import de.kalypzo.essentials.command.user.HealCommand;
import de.kalypzo.essentials.command.user.LoomCommand;
import de.kalypzo.essentials.command.user.PayCommand;
import de.kalypzo.essentials.command.user.PlaytimeCommand;
import de.kalypzo.essentials.command.user.SettingsCommand;
import de.kalypzo.essentials.command.user.SpeedCommand;
import de.kalypzo.essentials.command.user.StonecutterCommand;
import de.kalypzo.essentials.command.user.TogglePingCommand;
import de.kalypzo.essentials.command.user.TpaAcceptCommand;
import de.kalypzo.essentials.command.user.TpaCommand;
import de.kalypzo.essentials.command.user.WorkbenchCommand;
import de.kalypzo.essentials.command.world.DelHomeCommand;
import de.kalypzo.essentials.command.world.FarmweltCommand;
import de.kalypzo.essentials.command.world.HomeCommand;
import de.kalypzo.essentials.command.world.NetherCommand;
import de.kalypzo.essentials.command.world.SetHomeCommand;
import de.kalypzo.essentials.command.world.SpawnCommand;
import de.kalypzo.essentials.command.world.TeleportCommand;
import de.kalypzo.essentials.command.world.TeleportLocationCommand;
import de.kalypzo.essentials.command.world.WarpCommand;
import de.kalypzo.essentials.command.world.WarpsCommand;
import de.kalypzo.essentials.environment.PluginEnvironment;
import de.kalypzo.essentials.exception.ComponentException;
import de.kalypzo.essentials.exception.TransactionException;
import de.kalypzo.essentials.user.EssentialsOfflineUser;
import de.kalypzo.essentials.user.EssentialsUser;
import de.kalypzo.essentials.user.OnlineUsers;
import de.kalypzo.essentials.user.home.Home;
import de.kalypzo.essentials.util.Text;
import de.kalypzo.essentials.util.servername.InternalServerName;
import de.kalypzo.essentials.world.warps.Warp;
import it.einjojo.economy.TransactionStatus;
import it.einjojo.economy.exception.EconomyException;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import studio.mevera.imperat.BukkitCommandSource;
import studio.mevera.imperat.BukkitImperat;
import studio.mevera.imperat.annotations.base.element.MethodElement;
import studio.mevera.imperat.command.returns.BaseReturnResolver;
import studio.mevera.imperat.context.ExecutionContext;
import studio.mevera.imperat.util.TypeWrap;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Creates commands using
 * <a href="https://mevera.studio/docs/Imperat/CreateYourFirstCommand">imperat</a>
 */
public class ImperatCommandLoader {
    private final EssentialsPlugin plugin;
    private final BukkitImperat imperat;

    public ImperatCommandLoader(EssentialsPlugin plugin) {
        this.plugin = plugin;
        imperat = BukkitImperat.builder(plugin, true).build();
    }

    public void load() {
        registerReturnResolver();
        registerArgTypes();
        registerErrorHandlers();
        registerCommands();
        plugin.getComponentLogger().info(Text.deserialize("<ss>Commands registriert"));
    }

    private void registerReturnResolver() {
        imperat.config().registerReturnResolver(
            new TypeWrap<CompletableFuture<Void>>() {}.getType(),
                new BaseReturnResolver<>(new TypeWrap<CompletableFuture<Void>>() {
                }) {
                    @Override
                    public void handle(ExecutionContext<BukkitCommandSource> context, MethodElement method, CompletableFuture<Void> future) {
                        future.exceptionally(ex -> {
                            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                            imperat.config().handleExecutionError(cause, context, method.getElement().getDeclaringClass(), method.getName());
                            return null;
                        });
                    }
                }
        );
    }

    private void registerArgTypes() {
        PluginEnvironment env = plugin.getEnvironment();
        imperat.config().registerArgType(EssentialsUser.class, new EssentialsUserType(env));
        imperat.config().registerArgType(EssentialsOfflineUser.class, new EssentialsOfflineUserType(env));
        imperat.config().registerArgType(Home.class, new HomeType());
        imperat.config().registerArgType(Warp.class, new WarpType());
        imperat.config().registerArgType(GameMode.class, new GameModeType());
        imperat.config().registerArgType(OnlineUsers.class, new OnlineUsersType(env));
        imperat.config().registerArgType(PayTarget.class, new PayTargetType(env));
    }

    private void registerErrorHandlers() {
        imperat.config().setErrorHandler(ComponentException.class, (ex, ctx) ->
            ctx.source().origin().sendMessage(ex));
        imperat.config().setErrorHandler(EconomyException.class, (ex, ctx) -> {
            plugin.getSLF4JLogger().error("Economy error by {}", ctx.source().name(), ex);
            ctx.source().origin().sendMessage(Component.translatable("essentials.economy.error", Component.text("")));
        });
        imperat.config().setErrorHandler(TransactionException.class, (ex, ctx) -> {
            var status = ex.getStatus();
            if (status.equals(TransactionStatus.INSUFFICIENT_FUNDS)) {
                ctx.source().origin().sendMessage(Component.translatable("essentials.economy.insufficient-funds"));
            } else {
                ctx.source().origin().sendMessage(Component.translatable("essentials.economy.transaction-error",
                    Component.text(status.name().toUpperCase(Locale.ROOT))));
            }
        });
    }

    private void registerCommands() {
        imperat.registerCommands(
            new BroadcastCommand(),
            new DebugCommand(),
            new SendMessage(),
            new InfoCommand(),
            new DiscordCommand(),
            new ShopCommand(),
            new TeamChatCommand(),
            new MsgCommand(),
            new ReplyCommand(),
            new MsgCancelCommand(),
            new BackCommand(),
            new EnderchestCommand(),
            new FlyCommand(),
            new GameModeCommand(),
            new HeadCommand(),
            new FeedCommand(),
            new HealCommand(),
            new WorkbenchCommand(),
            new LoomCommand(),
            new AnvilCommand(),
            new StonecutterCommand(),
            new BalanceCommand(),
            new PayCommand(),
            new PlaytimeCommand(),
            new SettingsCommand(),
            new TogglePingCommand(),
            new SpeedCommand(),
            new TpaCommand(),
            new TpaAcceptCommand(),
            new HomeCommand(),
            new SetHomeCommand(),
            new DelHomeCommand(),
            new TeleportCommand(),
            new TeleportLocationCommand(),
            new WarpCommand(),
            new WarpsCommand(),
            new SpawnCommand(),
            new FarmweltCommand(),
            new NetherCommand()
        );

        if (plugin.getConfig().getBoolean("plot-cmd-proxy.enabled", true)) {
            String dest = plugin.getConfig().getString("plot-cmd-proxy.server", "citybuild");
            if (!dest.equals(InternalServerName.get())) {
                imperat.registerCommand(new ProxiedPlotCommand(dest));
                plugin.getComponentLogger().info(Text.deserialize("<ss>Registered Proxy-Plot command proxied to " + dest));
            }
        }

        if (plugin.getServer().getPluginManager().isPluginEnabled("LabyModServerAPI")) {
            imperat.registerCommand(new LabyModDebugCommand());
            plugin.getComponentLogger().info(Text.deserialize("<ss>Registered Labymod-Debug command"));
        }
    }
}
