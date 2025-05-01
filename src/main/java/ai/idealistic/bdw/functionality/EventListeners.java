package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.implementation.DiscordWebhook;
import ai.idealistic.bdw.listeners.AllEvents;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventListeners implements Listener {

    private static final Map<Integer, DiscordWebhook> listeners = new HashMap<>(1);

    static void update() {
        if (!Register.isPluginEnabled()) {
            return;
        }
        for (Map.Entry<String, Set<Class<? extends Event>>> entry : EventFinder.getAll()) {
            try {
                String pluginName = entry.getKey().replace(
                        ".jar",
                        ""
                ).replace(
                        "-",
                        "_"
                ).replace(
                        ".",
                        "_dot_"
                ).toLowerCase();

                for (Class<? extends Event> clazz : entry.getValue()) {
                    Method getHandlerList = clazz.getMethod("getHandlerList");
                    getHandlerList.setAccessible(true);
                    PluginManager pm = Register.plugin.getServer().getPluginManager();
                    int hashCode = clazz.hashCode();
                    DiscordWebhook cache = listeners.get(hashCode);

                    if (cache == null) {
                        DiscordWebhook webhook = new DiscordWebhook(
                                pluginName,
                                clazz
                        );
                        listeners.put(clazz.hashCode(), webhook);

                        pm.registerEvent(
                                clazz,
                                new AllEvents(),
                                EventPriority.HIGHEST,
                                (l, event) -> webhook.send(event),
                                Register.plugin
                        );
                    } else {
                        cache.reload();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

}
