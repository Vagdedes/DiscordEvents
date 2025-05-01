package ai.idealistic.bdw.functionality.placeholders;

import org.bukkit.Location;

public class LocationPlaceholders {

    public static String x(Location location) {
        return Integer.toString(location.getBlockX());
    }

    public static String y(Location location) {
        return Integer.toString(location.getBlockY());
    }

    public static String z(Location location) {
        return Integer.toString(location.getBlockZ());
    }

    // todo
}
