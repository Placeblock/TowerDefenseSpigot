package placeblock.towerdefense.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import placeblock.towerdefense.game.TDGame;

import java.util.ArrayList;
import java.util.Arrays;

public class AddWaypointSubCommand extends SubCommand {
    @Override
    public void onCommand(Player player, Command command, String[] args) {
        TDGame.addWaypoint(args[1], player.getLocation());
    }

    @Override
    public String getPermission() {
        return "create.addwaypoint";
    }
}