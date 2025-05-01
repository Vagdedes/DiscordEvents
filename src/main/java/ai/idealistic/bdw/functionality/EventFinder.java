package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.Register;
import org.bukkit.event.Event;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class EventFinder {

    private static final String bukkitPackage = "org.bukkit.event";
    private static final Map<Integer, Set<Class<? extends Event>>>
            events = new HashMap<>(1);

    static Collection<Class<? extends Event>> getAll() {
        Set<Class<? extends Event>> allEvents = new HashSet<>();

        for (Set<Class<? extends Event>> events : EventFinder.events.values()) {
            allEvents.addAll(events);
        }
        return allEvents;
    }

    static void update() {
        try {
            if (!events.containsKey(bukkitPackage.hashCode())) {
                Reflections bukkitReflections = new Reflections(bukkitPackage);
                events.put(
                        bukkitPackage.hashCode(),
                        bukkitReflections.getSubTypesOf(Event.class)
                );
            }
        } catch (Exception ignored) {
        }

        File pluginsDir = Register.plugin.getDataFolder().getParentFile();
        File[] pluginJars = pluginsDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (pluginJars != null) {
            for (File jarFile : pluginJars) {
                if (jarFile.isFile()) {
                    int hashCode = jarFile.getName().hashCode();

                    if (!events.containsKey(hashCode)) {
                        try {
                            URL jarUrl = jarFile.toURI().toURL();
                            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, Register.plugin.getClass().getClassLoader());

                            Reflections reflections = new Reflections(new ConfigurationBuilder()
                                    .addClassLoaders(classLoader)
                                    .setUrls(ClasspathHelper.forClassLoader(classLoader))
                                    .filterInputsBy(new FilterBuilder().include(".*"))
                            );
                            events.put(
                                    hashCode,
                                    reflections.getSubTypesOf(Event.class)
                            );
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

}
