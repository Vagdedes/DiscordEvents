package ai.idealistic.bdw.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandTab implements TabCompleter {

    private static final Map<String, Permission[]> commands = new LinkedHashMap<>(3); // Attention

    static {
        commands.put("toggle", new Permission[]{Permission.TOGGLE});
        commands.put("reload", new Permission[]{Permission.RELOAD});
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String arg, String[] args) {
        List<String> list = new ArrayList<>();
        boolean isPlayer = sender instanceof Player;
        Player p = isPlayer ? (Player) sender : null;
        String argAbstract = args[0].toLowerCase();

        for (Map.Entry<String, Permission[]> entry : commands.entrySet()) {
            boolean add;

            if (!isPlayer) {
                add = true;
            } else {
                add = false;

                if (entry.getValue().length > 0) {
                    for (Permission permission : entry.getValue()) {
                        if (Permissions.has(p, permission)) {
                            add = true;
                            break;
                        }
                    }
                } else {
                    add = Permissions.has(p, Permission.ADMIN);
                }
            }

            if (add) {
                String key = entry.getKey();

                if (key.contains(argAbstract)) {
                    list.add(key);
                }
            }
        }
        return list;
    }

}
