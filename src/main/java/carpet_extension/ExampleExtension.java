package carpet_extension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.settings.SettingsManager;
import carpet.utils.Messenger;

import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExampleExtension implements CarpetExtension, ModInitializer
{
	// This is our instance of a custom SettingsManager. This one is
	// not needed if you want to add your settings to Carpet.
	// This example covers both
    private static SettingsManager mySettingsManager;
    
    // This will be used to load our extension into Carpet. It must be
    // in this onInitialize method from Fabric Loader's entrypoints
    // or earlier.
    @Override
    public void onInitialize()
    {
    	// This will initialize our independent SettingsManager with its
    	// own command (which is the second parameter)
        mySettingsManager = new SettingsManager("1.0", "examplemod", "Example Mod");
        // And this will register everything in this class to Carpet
        CarpetServer.manageExtension(this);
    }

    // Check Javadocs for those methods to see when they trigger,
    // by checking the CarpetExtension interface
    @Override
    public void onGameStarted()
    {
        // Let /carpet handle our few simple settings.
    	// The settings in there will be added to the list of settings in
    	// Carpet's default command
        CarpetServer.settingsManager.parseSettingsClass(ExampleSimpleSettings.class);
        // Lets have our own settings class independent from carpet.conf
        // This will add those rules to our settings mangager.
        mySettingsManager.parseSettingsClass(ExampleOwnSettings.class);

        // set-up a snooper to observe how rules are changing in Carpet.
        // This will check for changes only in the SettingsManager from
        // /carpet. To add an observer to yours, just change the reference,
        // and if you want to watch for changes on any manager from any extension, 
        // use SettingsManager.addGlobalRuleObserver()
        CarpetServer.settingsManager.addRuleObserver( (serverCommandSource, currentRuleState, originalUserTest) ->
        {
            if (currentRuleState.categories.contains("examplemod"))
            {
            	// This is Carpet's messenger class. You can use it to easily message a ServerCommandSource or a player
            	// with its builtin methods, or to create Text instances.
            	// Messenger.m() will format the String using the formatting declared at the top of the Messenger class
            	// Check it out!
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
                	// print_server_message(MinecraftServer, String) will print the message to every player in the server.
                	// It will add some formatting by default, so it won't be configurable. If you wish to,
                	// you can use print_server_message(MinecraftServer, BaseText) with a BaseText created from Messenger.c() 
                    Messenger.print_server_message(
                            serverCommandSource.getMinecraftServer(),
                            "Ehlo everybody, "+serverCommandSource.getPlayer().getName().getString()+" is cheating..."
                    );
                }
                catch (CommandSyntaxException ignored) { }
            }
        });
    }
    
    @Override
    public String version() {
    	// Here you can add the name of your extension so Carpet knows who we are
    	// This is optional though
    	return "example-mod";
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is also handled by Carpet as an extension, since we claim own settings manager at customSettingsManager()
    	// You can remove the events you don't use
    }
    
    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
    	// Happens when the server has loaded itself AND the worlds
    	// You can remove this event if you don't need it
    }
    
    @Override
    public void onReload(MinecraftServer server) {
    	// Happens when the server reloads (usually /reload command)
    	// You can remove this event if you don't need it
    }
    
    @Override
    public void onServerClosed(MinecraftServer server) {
    	// Happens when the server closes. Can happen multiple times in singleplayer
    	// You can remove this event if you don't need it
    }

    @Override
    public void onTick(MinecraftServer server)
    {
        // no need to add this.
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher)
    {
    	// Place here all your command registration needs. Check an example in the
    	// ExampleCommand
    	// Our SettingsManager command is already handled by Carpet
        ExampleCommand.register(dispatcher);
    }

    @Override
    public SettingsManager customSettingsManager()
    {
        // this will ensure that our settings are loaded properly when world loads
    	// and commands and rule resets or reloads are handled correctly
        return mySettingsManager;
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player)
    {
        // You can remove this event if you don't need it
    }
    
    @Override
    public Map<String, String> canHasTranslations(String lang) {
    	// Here you can return a translation map for your rules and things
    	// for a given language.
    	// If you won't, you can remove this.
    	return CarpetExtension.super.canHasTranslations(lang);
    }
    
    @Override
    public void registerLoggers() {
    	// This occurs when Carpet is registering loggers.
    	// You can remove this event if you don't need it
    }
    
    @Override
    public void scarpetApi(CarpetExpression expression) {
    	// Use this to add your own methods to Scarpet
    	// Check javadocs for more details
    	// As with everything, you can remove this if you don't need it
    }
}
