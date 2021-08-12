package placeblock.towerdefense;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import placeblock.towerdefense.commands.TDCommand;
import placeblock.towerdefense.events.*;
import placeblock.towerdefense.game.TDEnemie;
import placeblock.towerdefense.game.TDGame;
import placeblock.towerdefense.game.TDTower;
import placeblock.towerdefense.game.TDWave;
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

        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntity(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerScroll(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPlaceBlock(), this);


        TDEnemie.saveConfig();
        TDTower.saveConfig();
        TDGame.saveConfig();
        TDWave.saveConfig();
    }

    @Override
    public void onDisable() {
        TDGame.unregisterAll();
    }
}
