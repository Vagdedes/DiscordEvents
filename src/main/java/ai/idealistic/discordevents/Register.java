package ai.idealistic.discordevents;

import ai.idealistic.discordevents.command.CommandExecution;
import ai.idealistic.discordevents.command.CommandTab;
import ai.idealistic.discordevents.functionality.BdwManager;
import ai.idealistic.discordevents.listeners.AllEvents;
import ai.idealistic.discordevents.listeners.ChunksEvent;
import ai.idealistic.discordevents.listeners.PluginStates;
import ai.idealistic.discordevents.utils.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin {

    public static Plugin plugin = null;
    public static final String
            command = "discordevents";

    public void onEnable() {
        plugin = this;
        new Metrics(this, 25770);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PluginStates(), this);
        pm.registerEvents(new AllEvents(), this);
        pm.registerEvents(new ChunksEvent(), this);
        BdwManager.update();

        this.getCommand(command).setExecutor(new CommandExecution());
        this.getCommand(command).setTabCompleter(new CommandTab());
    }

    public void onDisable() {
        plugin = this;
        BdwManager.disable();
    }

    public static boolean isPluginEnabled() {
        return plugin != null && plugin.isEnabled();
    }

}
