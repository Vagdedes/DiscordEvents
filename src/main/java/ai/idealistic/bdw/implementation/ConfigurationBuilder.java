package ai.idealistic.bdw.implementation;

import ai.idealistic.bdw.Register;
import lombok.Getter;
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
        this.file = new File(getDirectory(webhook.getPluginName() + "/" + webhook.getEventName()));
        create();
    }

    private static String getDirectory(String fileName) {
        return Register.plugin.getDataFolder() + "/" + fileName + ".yml";
    }

    public void clear() {
        bool.clear();
        ints.clear();
        dbls.clear();
        str.clear();
    }

    private void create() {
        addOption("options.enabled", false);

        addOption("webhook.avatar_url", "");
        addOption("webhook.color", "");
        addOption("webhook.author", "");
        addOption("webhook.author_url", "");
        addOption("webhook.author_icon_url", "");
        addOption("webhook.title", "");
        addOption("webhook.title_url", "");
        addOption("webhook.description", "");
        addOption("webhook.footer", "");
        addOption("webhook.footer_icon_url", "");
        addOption("webhook.content", "");

        for (int i = 1; i <= DiscordWebhook.MAX_WEBHOOK_URLS; i++) {
            addOption("webhook.url." + i, "");
        }
        for (int i = 1; i <= DiscordWebhook.MAX_FIELDS; i++) {
            addOption("webhook.fields." + i + ".name", "");
            addOption("webhook.fields." + i + ".value", "");
            addOption("webhook.fields." + i + ".inline", false);
        }
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
            value = "";
        }
        str.put(path, value);
        return value;
    }

    public void addOption(String path, Object value) {
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
                this.dbls.remove(path);
                this.ints.remove(path);
                this.str.remove(path);
                this.bool.remove(path);
            } catch (Exception ignored) {
            }
        }
    }

    public void setOption(String path, Object value) {
        YamlConfiguration filea = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
        }
        filea.set(path, value);

        try {
            filea.save(file);
            this.dbls.remove(path);
            this.ints.remove(path);
            this.str.remove(path);
            this.bool.remove(path);
        } catch (Exception ignored) {
        }
    }

}
