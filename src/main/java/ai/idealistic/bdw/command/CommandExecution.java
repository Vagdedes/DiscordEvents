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

            } else if (args.length == 3) {

            } else {

            }
        }
        return false;
    }

}
