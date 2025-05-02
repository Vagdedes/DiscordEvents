package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.implementation.DiscordWebhook;
import ai.idealistic.bdw.listeners.AllEvents;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EventListeners implements Listener {

    private static final Map<Integer, DiscordWebhook> listeners = new ConcurrentHashMap<>(1);

    static void update() {
        if (!Register.isPluginEnabled()) {
            return;
        }
        for (Map.Entry<String, Set<Class<? extends Event>>> entry : EventFinder.getAll()) {
            String pluginName = preparePluginName(entry.getKey());

            for (Class<? extends Event> clazz : entry.getValue()) {
                try {
                    if (clazz == null) {
                        continue;
                    }
                    // Skip abstract classes or interfaces
                    if (Modifier.isAbstract(clazz.getModifiers())
                            || clazz.isInterface()) {
                        continue;
                    }
                    // Skip if class is synthetic or anonymous (e.g. lambda-generated)
                    if (clazz.isSynthetic()
                            || clazz.isAnonymousClass()) {
                        continue;
                    }
                    // Check for getHandlerList method
                    Method getHandlerList = clazz.getDeclaredMethod("getHandlerList");

                    // Must be static
                    if (!Modifier.isStatic(getHandlerList.getModifiers())) {
                        continue;
                    }
                    // Try invoking it to make sure it's accessible and doesn't error
                    getHandlerList.setAccessible(true);
                    getHandlerList.invoke(null);

                    // All checks passed, register listener
                    PluginManager pm = Register.plugin.getServer().getPluginManager();
                    int hashCode = prepareEventHashCode(pluginName, clazz.getSimpleName());
                    DiscordWebhook cache = listeners.get(hashCode);

                    if (cache == null) {
                        DiscordWebhook webhook = new DiscordWebhook(pluginName, clazz);
                        pm.registerEvent(
                                clazz,
                                new AllEvents(),
                                EventPriority.HIGHEST,
                                (listener, event) -> webhook.send(event),
                                Register.plugin
                        );
                        webhook.reload();
                        listeners.put(hashCode, webhook);
                    } else {
                        cache.reload();
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static String preparePluginName(String s) {
        return s.replace(
                ".jar",
                ""
        ).replace(
                "-",
                "_"
        ).replace(
                ".",
                "_dot_"
        ).toLowerCase();
    }

    private static int prepareEventHashCode(String pluginName, String s) {
        return (pluginName.hashCode() * 31) + s.toLowerCase().hashCode();
    }

    static DiscordWebhook getWebhook(String pluginName, String eventName) {
        return listeners.get(prepareEventHashCode(
                preparePluginName(pluginName),
                eventName
        ));
    }

}
