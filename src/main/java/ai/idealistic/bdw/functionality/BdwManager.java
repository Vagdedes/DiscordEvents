package ai.idealistic.bdw.functionality;

import ai.idealistic.bdw.utils.GeneralThread;

public class BdwManager {

    private static final GeneralThread.ThreadPool
            thread = new GeneralThread.ThreadPool(50L);

    private static final Object lock = new Object();
    private static boolean isRunning = false;

    public static void update() {
        synchronized (lock) {
            if (isRunning) {
                return;
            }
            isRunning = thread.executeIfFree(() -> {
                EventFinder.update(); // Always first
                EventListeners.update();
                isRunning = false;
            });
        }
    }

}
