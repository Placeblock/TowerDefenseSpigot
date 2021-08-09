package placeblock.towerdefense.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import placeblock.towerdefense.commands.subcommands.*;

import java.util.HashMap;
import java.util.Map;

public class TDCommand implements CommandExecutor {

    private Map<String, SubCommand> commands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if(!commands.containsKey(args[0].toLowerCase())) {
            p.sendMessage("§cThis Command does not exist");
            return false;
        }

        if(!p.hasPermission("towerdefense." + commands.get(args[0]).getPermission()))

        commands.get(args[0]).onCommand(p, command, args);
        return true;
    }

    public void registerCommand(String cmd, SubCommand subCommand) {
        commands.put(cmd, subCommand);
    }

    public void registerCommands() {
        registerCommand("createenemie", new EnemieSubCommand());
        registerCommand("createtower", new TowerSubCommand());
        registerCommand("addwaveenemie", new WaveSubCommand());
        registerCommand("start", new GameSubCommand());
    }
}
