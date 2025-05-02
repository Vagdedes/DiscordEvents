package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.implementation.DiscordWebhook;
import ai.idealistic.bdw.utils.WebhookThread;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BdwManager {

    private static final ExecutorService
            thread = Executors.newSingleThreadExecutor();

    public static void update() {
        thread.submit(() -> {
            EventFinder.update(); // Always first
            EventListeners.update();
        });
    }

    public static void update(Plugin plugin) {
        thread.submit(() -> {
            EventFinder.update(plugin); // Always first
            EventListeners.update();
        });
    }

    public static void disable() {
        thread.shutdown();
        WebhookThread.shutdown();
    }

    public static Boolean toggle(String pluginName, String eventName) {
        DiscordWebhook webhook = EventListeners.getWebhook(pluginName, eventName);

        if (webhook == null) {
            return null;
        }
        boolean opposite = !webhook.isEnabled();
        webhook.setEnabled(opposite);
        return opposite;
    }

    public static Boolean setOption(
            String pluginName,
            String eventName,
            String optionName,
            String optionValue
    ) {
        optionName = optionName.toLowerCase();

        if (optionName.startsWith("information.")) {
            return null;
        }
        DiscordWebhook webhook = EventListeners.getWebhook(pluginName, eventName);

        if (webhook == null) {
            return null;
        }
        return webhook.setOptionIfExists(optionName, optionValue);
    }

}
