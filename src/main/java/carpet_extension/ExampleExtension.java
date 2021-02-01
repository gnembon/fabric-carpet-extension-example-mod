package carpet_extension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.Expression;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;
import carpet_extension.script.ExampleScarpetEvent;
import carpet_extension.script.ExampleScarpetFunctions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleExtension implements CarpetExtension
{
    public static void noop() { }
    private static SettingsManager mySettingManager;
    public static final String identifier = "examplemod";
    public static final String fancyName = "Carpet Example Mod";
    public static final Logger LOGGER = LogManager.getLogger(fancyName);
    public static MinecraftServer minecraft_server;
    static
    {
        mySettingManager = new SettingsManager("1.0",identifier,fancyName);
        CarpetServer.manageExtension(new ExampleExtension());
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(ExampleSimpleSettings.class);
        // Lets have our own settings class independent from carpet.conf
        mySettingManager.parseSettingsClass(ExampleOwnSettings.class);

        // set-up a snooper to observe how rules are changing in carpet
        CarpetServer.settingsManager.addRuleObserver( (serverCommandSource, currentRuleState, originalUserTest) ->
        {
            if (currentRuleState.categories.contains("examplemod"))
            {
                Messenger.m(
                        serverCommandSource,
                        "gi Psssst... make sure not to change not to touch original carpet rules"
                );
                // obviously you can change original carpet rules
            }
            else
            {
                try
                {
                    Messenger.print_server_message(
                            serverCommandSource.getMinecraftServer(),
                            "Ehlo everybody, "+serverCommandSource.getPlayer().getName().getString()+" is cheating..."
                    );
                }
                catch (CommandSyntaxException ignored) { }
            }
        });

        ExampleScarpetEvent.noop(); // To load the event properly
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is handled as an extension, since we claim own settings manager

        // setting minecraft_server here so I can use to declare event
        minecraft_server = server;
    }

    @Override
    public void onTick(MinecraftServer server)
    {
        // no need to add this.
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        ExampleCommand.register(dispatcher);
    }

    @Override
    public SettingsManager customSettingsManager()
    {
        // this will ensure that our settings are loaded properly when world loads
        return mySettingManager;
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
        //
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        //
    }

    @Override
    public void scarpetApi(CarpetExpression expression){
        ExampleScarpetFunctions.apply(expression.getExpr());
        LOGGER.info("Loaded Carpet-Extension scarpet extension");
    }
}
