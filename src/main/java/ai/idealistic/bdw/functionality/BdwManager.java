package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.utils.WebhookThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BdwManager {

    private static final ExecutorService thread = Executors.newSingleThreadExecutor();

    private static final Runnable runnable = () -> {
        EventFinder.update(); // Always first
        EventListeners.update();
    };

    public static void update() {
        thread.submit(runnable);
    }

    public static void disable() {
        thread.shutdown();
        WebhookThread.shutdown();
    }

}
