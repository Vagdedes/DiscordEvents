package ai.idealistic.bdw.listeners;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.functionality.BdwManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginStates implements Listener {

    static {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Register.plugin, BdwManager::update, 0L, 1200L);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void enable(PluginEnableEvent e) {
        BdwManager.update(e.getPlugin());
    }

}
