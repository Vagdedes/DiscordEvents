package ai.idealistic.bdw.functionality;

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

}
