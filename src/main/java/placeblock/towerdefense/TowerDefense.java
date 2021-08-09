package placeblock.towerdefense;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import placeblock.towerdefense.commands.TDCommand;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.registry.TDPlayerRegistry;

public final class TowerDefense extends JavaPlugin {

    @Getter private static TowerDefense instance;
    @Getter private TDPlayerRegistry playerRegistry;

    @Override
    public void onEnable() {
        instance = this;

        playerRegistry = new TDPlayerRegistry();

        TDCommand command = new TDCommand();
        command.registerCommands();
        getCommand("towerdefense").setExecutor(command);
    }

    @Override
    public void onDisable() {
        TDGame.unregisterAll();
    }
}
