package ai.idealistic.bdw.command;

import ai.idealistic.bdw.Register;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandExecution implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if ((label.equalsIgnoreCase(Register.command)
                || label.equalsIgnoreCase(Register.shortCommand))
                && (isPlayer
                || sender instanceof ConsoleCommandSender)) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {

                } else {
                    def(sender, isPlayer);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("toggle")) {

                } else {
                    def(sender, isPlayer);
                }
            } else {
                def(sender, isPlayer);
            }
        }
        return false;
    }

    private static void def(CommandSender sender, boolean isPlayer) {
        if (!isPlayer
                || Permissions.has((Player) sender, Permission.TOGGLE)
                || Permissions.has((Player) sender, Permission.RELOAD)) {
            sender.sendMessage("");

            if (!isPlayer
                    || Permissions.has((Player) sender, Permission.RELOAD)) {
                sender.sendMessage("§a/" + Register.command + " reload");
                sender.sendMessage("§a/" + Register.shortCommand + " reload");
            }
            if (!isPlayer
                    || Permissions.has((Player) sender, Permission.TOGGLE)) {
                sender.sendMessage("§a/" + Register.command + " toggle <plugin-name> <event-name>");
                sender.sendMessage("§a/" + Register.shortCommand + " toggle <plugin-name> <event-name>");
            }
            sender.sendMessage("");
        } else {
            sender.sendMessage("§cYou do not have permission to use this command.");
        }
    }

}
