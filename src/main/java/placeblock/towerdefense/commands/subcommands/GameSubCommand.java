package placeblock.towerdefense.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import placeblock.towerdefense.game.TDGame;

import java.util.ArrayList;
import java.util.Arrays;

public class GameSubCommand extends SubCommand {
    @Override
    public void onCommand(Player player, Command command, String[] args) {
        new TDGame(args[0], args[1], new ArrayList<>(Arrays.asList(player)));
    }

    @Override
    public String getPermission() {
        return "start";
    }
}
