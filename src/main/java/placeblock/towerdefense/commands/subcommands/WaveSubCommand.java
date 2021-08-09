package placeblock.towerdefense.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import placeblock.towerdefense.game.TDWave;

public class WaveSubCommand extends SubCommand{


    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if(!args[2].matches("\\d+") || !args[4].matches("\\d+")) {
            p.sendMessage("Wrong Usage! [wave] [time] [enemie] [count]");
            return;
        }

        for(int i = 0; i < Integer.valueOf(args[4]); i++) {
            TDWave.addTimeEnemie(args[1], Integer.valueOf(args[2]), args[3]);
        }
    }

    @Override
    public String getPermission() {
        return "create.wave";
    }
}
