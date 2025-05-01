package ai.idealistic.bdw.command;

import org.bukkit.entity.Player;

public class Permissions {

    public static boolean has(Player p, Permission permission) {
        return permission != Permission.ADMIN
                ? p.hasPermission(Permission.ADMIN.getKey())
                || p.hasPermission(permission.getKey())
                : p.hasPermission(permission.getKey());
    }

}
