package placeblock.towerdefense.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import placeblock.towerdefense.game.TDEnemie;

public class EnemieSubCommand extends SubCommand {


    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if(!args[3].matches("\\d+") || !args[4].matches("\\d+") || !args[5].matches("\\d+")) {
            p.sendMessage("Wrong Usage! [name] [type] [health] [speed] [damage]");
            return;
        }
        ItemStack[] armorcontents = p.getInventory().getArmorContents();
        Material helmet = Material.AIR;
        if(armorcontents[0] != null) helmet = armorcontents[0].getType();
        Material chestplate = Material.AIR;
        if(armorcontents[0] != null) chestplate = armorcontents[1].getType();
        Material leggings = Material.AIR;
        if(armorcontents[0] != null) leggings = armorcontents[2].getType();
        Material boots = Material.AIR;
        if(armorcontents[0] != null) boots = armorcontents[3].getType();

        TDEnemie.registerEnemie(
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                args[1],
                helmet,
                leggings,
                chestplate,
                boots,
                EntityType.valueOf(args[2])
        );
    }

    @Override
    public String getPermission() {
        return "create.enemie";
    }
}
