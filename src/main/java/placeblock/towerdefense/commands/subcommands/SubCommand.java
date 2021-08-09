package placeblock.towerdefense.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract void onCommand(Player player, Command command, String[] args);

    public abstract String getPermission();

}
