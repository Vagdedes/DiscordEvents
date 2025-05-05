package ai.idealistic.discordevents.listeners;

import ai.idealistic.discordevents.functionality.BdwManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginStates implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void enable(PluginEnableEvent e) {
        BdwManager.update(e.getPlugin());
    }

}
