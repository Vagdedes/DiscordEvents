package ai.idealistic.bdw;

import ai.idealistic.bdw.functionality.BdwManager;
import ai.idealistic.bdw.listeners.AllEvents;
import ai.idealistic.bdw.listeners.PluginStates;
import ai.idealistic.bdw.utils.GeneralThread;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Register extends JavaPlugin {

    public static Plugin plugin = null;

    public void onEnable() {
        plugin = this;
        Logger.getLogger("org.reflections").setLevel(java.util.logging.Level.OFF);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PluginStates(), this);
        pm.registerEvents(new AllEvents(), this);
        BdwManager.update();
    }

    public void onDisable() {
        plugin = this;
        GeneralThread.disable();
    }

    public static boolean isPluginEnabled() {
        return plugin != null && plugin.isEnabled();
    }

}
