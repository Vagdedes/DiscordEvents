package ai.idealistic.bdw;

import ai.idealistic.bdw.command.CommandExecution;
import ai.idealistic.bdw.command.CommandTab;
import ai.idealistic.bdw.functionality.BdwManager;
import ai.idealistic.bdw.listeners.AllEvents;
import ai.idealistic.bdw.listeners.PluginStates;
import ai.idealistic.bdw.utils.Metrics;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin {

    public static Plugin plugin = null;
    public static final String
            command = "bukkitdiscordwebhooks",
            shortCommand = "bdw";

    public void onEnable() {
        plugin = this;
        //new Metrics(this, 00000); todo

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PluginStates(), this);
        pm.registerEvents(new AllEvents(), this);
        BdwManager.update();

        this.getCommand(command).setExecutor(new CommandExecution());
        this.getCommand(command).setTabCompleter(new CommandTab());

        this.getCommand(shortCommand).setExecutor(new CommandExecution());
        this.getCommand(shortCommand).setTabCompleter(new CommandTab());
    }

    public void onDisable() {
        plugin = this;
        BdwManager.disable();
    }

    public static boolean isPluginEnabled() {
        return plugin != null && plugin.isEnabled();
    }

}
