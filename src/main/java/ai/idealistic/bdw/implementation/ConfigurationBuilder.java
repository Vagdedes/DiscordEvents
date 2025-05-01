package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.Register;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationBuilder {

    @Getter
    private final DiscordWebhook webhook;
    private final File file;
    private final Map<String, Boolean>
            bool = new ConcurrentHashMap<>(1);
    private final Map<String, Integer> ints = new ConcurrentHashMap<>(1);
    private final Map<String, Double> dbls = new ConcurrentHashMap<>(1);
    private final Map<String, String> str = new ConcurrentHashMap<>(1);

    public ConfigurationBuilder(DiscordWebhook webhook) {
        this.webhook = webhook;
        this.file = new File(getDirectory("")); // todo
        create();
    }

    private static String getDirectory(String fileName) {
        return Register.plugin.getDataFolder() + "/" + fileName + ".yml";
    }


    private void create() {
        // todo webhook urls
        addOption("avatar_url", "");
        addOption("color", "");
        addOption("author", "");
        addOption("author_url", "");
        addOption("author_icon_url", "");
        addOption("title", "");
        addOption("title_url", "");
        addOption("description", "");
        addOption("footer", "");
        addOption("footer_icon_url", "");
        addOption("content", "");
        // todo fields
    }

    private YamlConfiguration getPath() {
        if (!file.exists()) {
            create();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public boolean getBoolean(String path) {
        Boolean data = bool.get(path);

        if (data != null) {
            return data;
        }
        boolean value = getPath().getBoolean(path);
        bool.put(path, value);
        return value;
    }

    public int getInteger(String path) {
        Integer data = ints.get(path);

        if (data != null) {
            return data;
        }
        int value = getPath().getInt(path);
        ints.put(path, value);
        return value;
    }

    public double getDouble(String path) {
        Double data = dbls.get(path);

        if (data != null) {
            return data;
        }
        double value = getPath().getDouble(path);
        dbls.put(path, value);
        return value;
    }

    public String getString(String path) {
        String data = str.get(path);

        if (data != null) {
            return data;
        }
        String value = getPath().getString(path);

        if (value == null) {
            return path;
        }
        str.put(path, value);
        return value;
    }

    public String getColorfulString(String path) {
        String data = str.get(path);

        if (data != null) {
            return data;
        }
        if (!file.exists()) {
            create();
        }
        String value = getPath().getString(path);

        if (value == null) {
            return path;
        } else {
            value = ChatColor.translateAlternateColorCodes('&', value);
        }
        str.put(path, value);
        return value;
    }

    private void addOption(String path, Object value) {
        YamlConfiguration filea = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
        }
        if (!filea.contains(path)) {
            filea.set(path, value);

            try {
                filea.save(file);
            } catch (Exception ignored) {
            }
        }
    }

    public void clear() {
        bool.clear();
        ints.clear();
        str.clear();
    }

}
