package placeblock.towerdefense;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import placeblock.towerdefense.commands.TDCommand;
import placeblock.towerdefense.registry.TDEnemieRegistry;
import placeblock.towerdefense.registry.TDPlayerRegistry;
import placeblock.towerdefense.registry.TDTowerRegistry;

public final class TowerDefense extends JavaPlugin {

    @Getter private static TowerDefense instance;
    @Getter private TDEnemieRegistry enemieRegistry;
    @Getter private TDTowerRegistry towerRegistry;
    @Getter private TDPlayerRegistry playerRegistry;

    @Override
    public void onEnable() {
        instance = this;

        enemieRegistry = new TDEnemieRegistry();
        enemieRegistry.loadEnemies();
        towerRegistry = new TDTowerRegistry();
        towerRegistry.loadTowers();
        playerRegistry = new TDPlayerRegistry();

        TDCommand command = new TDCommand();
        command.registerCommands();
        getCommand("towerdefense").setExecutor(command);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
