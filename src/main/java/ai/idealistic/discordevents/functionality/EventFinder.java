package ai.idealistic.discordevents.functionality;

import ai.idealistic.discordevents.Register;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

public class EventFinder {

    public static final String[] nativePackages = new String[]{
            "org.bukkit.event",
            "org.spigotmc.event",
            "io.papermc.paper.event",
            "com.destroystokyo.paper.event"
    };
    private static final Map<String, Set<Class<? extends Event>>>
            events = new HashMap<>(1);

    static Set<Map.Entry<String, Set<Class<? extends Event>>>> getAll() {
        return events.entrySet();
    }

    private static String toRegexPackage(String input) {
        if (input == null || input.isEmpty()) return ".*";
        return input.replace(".", "\\.") + "\\..*";
    }

    static void update() {
        for (String nativePackage : nativePackages) {
            try {
                Set<Class<? extends Event>> set = events.get(nativePackage);

                if (set == null
                        || set.isEmpty()) {
                    forceLoadClasses(nativePackage, Bukkit.class.getClassLoader());
                    ClassLoader bukkitCL = Bukkit.class.getClassLoader();
                    Reflections bukkitReflections = new Reflections(new ConfigurationBuilder()
                            .addClassLoaders(bukkitCL)
                            .setUrls(ClasspathHelper.forPackage(nativePackage, bukkitCL))
                            .setScanners(new SubTypesScanner(false))
                            .filterInputsBy(new FilterBuilder().include(toRegexPackage(nativePackage)))
                    );
                    events.put(
                            nativePackage,
                            bukkitReflections.getSubTypesOf(Event.class)
                    );
                }
            } catch (Exception ignored) {
            }
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
                forceLoadPluginClasses(plugin);
                ClassLoader pluginClassLoader = plugin.getClass().getClassLoader();
                URL jarUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
                Reflections reflections = new Reflections(new ConfigurationBuilder()
                        .addClassLoaders(pluginClassLoader)
                        .setUrls(jarUrl)
                        .setScanners(new SubTypesScanner(false))
                        .filterInputsBy(new FilterBuilder().include(".*"))
                );

                Set<Class<? extends Event>> eventClasses = reflections.getSubTypesOf(Event.class);
                events.put(pluginName, eventClasses);
            } catch (Exception ignored) {
            }
        }
    }

    private static void forceLoadClasses(String basePackage, ClassLoader classLoader) {
        String path = basePackage.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                try (InputStream in = resource.openStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                    File file = new File(resource.toURI());
                    if (!file.exists()) continue;

                    Files.walk(file.toPath())
                            .filter(p -> p.toString().endsWith(".class"))
                            .forEach(p -> {
                                String fullPath = p.toString().replace(File.separatorChar, '.');
                                int start = fullPath.indexOf(basePackage);
                                String className = fullPath.substring(start, fullPath.length() - 6);

                                if (className.isEmpty() || className.contains("$")) return;
                                try {
                                    Class.forName(className, false, classLoader);
                                } catch (Throwable ignored) {
                                }
                            });

                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static void forceLoadPluginClasses(Plugin plugin) {
        if (plugin.equals(Register.plugin)) {
            return;
        }
        try {
            URL jarUrl = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
            try (JarFile jarFile = new JarFile(new File(jarUrl.toURI()))) {
                URLClassLoader loader = (URLClassLoader) plugin.getClass().getClassLoader();
                jarFile.stream()
                        .filter(e -> e.getName().endsWith(".class"))
                        .filter(e -> !e.getName().startsWith("META-INF/"))
                        .filter(e -> !e.getName().endsWith("module-info.class"))
                        .forEach(entry -> {
                            String className = entry.getName()
                                    .replace('/', '.')
                                    .replace(".class", "");
                            if (className.isEmpty() || className.contains("$")) return;
                            try {
                                Class.forName(className, false, loader);
                            } catch (Throwable ignored) {
                            }
                        });
            }
        } catch (Exception ignored) {
        }
    }

}
