package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.utils.AwarenessNotifications;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EventFinder {

    public static final String bukkitPackage = "org.bukkit.event";
    private static final Map<String, Set<Class<? extends Event>>>
            events = new HashMap<>(1);

    static Set<Map.Entry<String, Set<Class<? extends Event>>>> getAll() {
        return events.entrySet();
    }

    static void forceUpdate() {
        events.clear();
        update();
    }

    static void update() {
        try {
            Set<Class<? extends Event>> set = events.get(bukkitPackage);

            if (set == null
                    || set.isEmpty()) {
                Reflections bukkitReflections = new Reflections(new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forClassLoader(Bukkit.class.getClassLoader()))
                        .setScanners(new SubTypesScanner(false))
                        .filterInputsBy(new FilterBuilder().include("org\\.bukkit\\.event\\..*"))
                );
                events.put(
                        bukkitPackage,
                        bukkitReflections.getSubTypesOf(Event.class)
                );
            }
        } catch (Exception exception) {
            AwarenessNotifications.send(exception.getMessage());
        }

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            update(plugin);
        }
    }

    static void update(Plugin plugin) {
        String pluginName = plugin.getName();
        Set<Class<? extends Event>> set = events.get(pluginName);

        if (set == null
                || set.isEmpty()) {
            try {
                ClassLoader pluginClassLoader = plugin.getClass().getClassLoader();

                Reflections reflections = new Reflections(new ConfigurationBuilder()
                        .addClassLoaders(pluginClassLoader)
                        .setUrls(ClasspathHelper.forClassLoader(pluginClassLoader))
                        .setScanners(new SubTypesScanner(false))
                        .filterInputsBy(new FilterBuilder().include(".*"))
                );

                Set<Class<? extends Event>> eventClasses = reflections.getSubTypesOf(Event.class);
                events.put(pluginName, eventClasses);
            } catch (Exception exception) {
                AwarenessNotifications.send(exception.getMessage());
            }
        }
    }

}
