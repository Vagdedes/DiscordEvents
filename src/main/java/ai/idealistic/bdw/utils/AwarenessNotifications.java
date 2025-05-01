package ai.idealistic.bdw.utils;

import ai.idealistic.bdw.Register;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AwarenessNotifications {

    public static String getNotification(String s) {
        return "§8[§2" + Register.plugin.getName() + "§8]§a " + s;
    }

    public static void send(String s) {
        send(null, s);
    }

    public static void send(Player p, String s) {
        String message = getNotification(s);

        if (p != null && p.isOnline()) {
            p.sendMessage(message);
            Bukkit.getConsoleSender().sendMessage("(" + p.getName() + ") " + message);
        } else {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

}
