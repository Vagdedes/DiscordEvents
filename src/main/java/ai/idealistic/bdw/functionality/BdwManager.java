package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.utils.WebhookThread;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BdwManager {

    private static final ScheduledExecutorService
            thread = Executors.newSingleThreadScheduledExecutor();

    static {
        thread.scheduleAtFixedRate(
                BdwManager::forceUpdate,
                60_000L,
                60_000L,
                TimeUnit.MILLISECONDS
        );
    }

    public static void update() {
        thread.submit(() -> {
            EventFinder.update(); // Always first
            EventListeners.update();
        });
    }

    private static void forceUpdate() {
        thread.submit(() -> {
            EventFinder.forceUpdate(); // Always first
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
