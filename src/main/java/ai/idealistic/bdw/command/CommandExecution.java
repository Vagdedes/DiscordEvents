package ai.idealistic.bdw.command;

import ai.idealistic.bdw.Register;
import ai.idealistic.bdw.functionality.BdwManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandExecution implements CommandExecutor {

    private static final String NO_PERMISSION = "§cYou do not have permission to use this command.";

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = sender instanceof Player;

        if ((label.equalsIgnoreCase(Register.command)
                || label.equalsIgnoreCase(Register.shortCommand))
                && (isPlayer
                || sender instanceof ConsoleCommandSender)) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (isPlayer
                            && !Permissions.has((Player) sender, Permission.RELOAD)) {
                        sender.sendMessage(NO_PERMISSION);
                        return true;
                    }
                    BdwManager.update();
                    sender.sendMessage("§aThe plugin has started reloading, this may take a while to complete.");
                } else {
                    def(sender, isPlayer);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("toggle")) {
                    if (isPlayer
                            && !Permissions.has((Player) sender, Permission.RELOAD)) {
                        sender.sendMessage(NO_PERMISSION);
                        return true;
                    }
                    Boolean toggle = BdwManager.toggle(args[1], args[2]);

                    if (toggle == null) {
                        sender.sendMessage("§eThe Discord Webhook of this plugin or event could not be found.");
                    } else if (toggle) {
                        sender.sendMessage("§aThe Discord Webhook '" + args[2] + "' of the plugin '" + args[1] + "' has been enabled.");
                    } else {
                        sender.sendMessage("§cThe Discord Webhook '" + args[2] + "' of the plugin '" + args[1] + "' has been disabled.");
                    }
                } else {
                    def(sender, isPlayer);
                }
            } else if (args.length >= 5) {
                if (args[0].equalsIgnoreCase("set-option")) {
                    if (isPlayer
                            && !Permissions.has((Player) sender, Permission.OPTION)) {
                        sender.sendMessage(NO_PERMISSION);
                        return true;
                    }
                    StringBuilder value = new StringBuilder();

                    for (int i = 4; i < args.length; i++) {
                        value.append(args[i]).append(" ");
                    }
                    value = new StringBuilder(value.toString().trim());
                    Boolean option = BdwManager.setOption(args[1], args[2], args[3], value.toString());

                    if (option == null) {
                        sender.sendMessage("§eThe Discord Webhook of this plugin or event could not be found.");
                    } else if (!option) {
                        sender.sendMessage("§cThe Discord Webhook '" + args[2] + "' of the plugin '" + args[1] + "' has no such option.");
                    } else {
                        sender.sendMessage("§aThe Discord Webhook '" + args[2] + "' of the plugin '" + args[1] + "' has been modified.");
                    }
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
            sender.sendMessage(NO_PERMISSION);
        }
    }

}
