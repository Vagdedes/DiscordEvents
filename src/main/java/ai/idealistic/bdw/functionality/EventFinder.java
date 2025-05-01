package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.utils.AwarenessNotifications;
import org.bukkit.event.Event;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

    static void update() {
        try {
            if (!events.containsKey(bukkitPackage)) {
                Reflections bukkitReflections = new Reflections(bukkitPackage);
                events.put(
                        bukkitPackage,
                        bukkitReflections.getSubTypesOf(Event.class)
                );
            }
        } catch (Exception exception) {
            AwarenessNotifications.send(exception.getMessage());
        }

        File pluginsDir = Register.plugin.getDataFolder().getParentFile();
        File[] pluginJars = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (pluginJars != null) {
            for (File jarFile : pluginJars) {
                if (jarFile.isFile()) {
                    String pluginName = jarFile.getName();

                    if (!events.containsKey(pluginName)) {
                        try {
                            URL jarUrl = jarFile.toURI().toURL();
                            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, Register.plugin.getClass().getClassLoader());

                            Reflections reflections = new Reflections(new ConfigurationBuilder()
                                    .addClassLoaders(classLoader)
                                    .setUrls(ClasspathHelper.forClassLoader(classLoader))
                                    .filterInputsBy(new FilterBuilder().include(".*"))
                            );
                            events.put(
                                    pluginName,
                                    reflections.getSubTypesOf(Event.class)
                            );
                        } catch (Exception exception) {
                            AwarenessNotifications.send(exception.getMessage());
                        }
                    }
                }
            }
        }
    }

}
