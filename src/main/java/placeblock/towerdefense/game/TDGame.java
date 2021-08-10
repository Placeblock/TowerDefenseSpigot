package placeblock.towerdefense.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TDGame implements Listener {
    @Getter private final static ArrayList<TDGame> games = new ArrayList<>();

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/levels.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    private final static File gamefile = new File(TowerDefense.getInstance().getDataFolder() + "/games.yml");
    private final static YamlConfiguration gamedata = YamlConfiguration.loadConfiguration(gamefile);

    @Getter private final String name;

    @Getter private final ArrayList<TDPlayer> players = new ArrayList<>();
    @Getter private final ArrayList<TDTower> towers = new ArrayList<>();
    @Getter private final ArrayList<TDEnemie> enemies = new ArrayList<>();

    @Getter private final ArrayList<Location> path;

    private final List<String> waves;
    @Getter private TDWave activeWave;

    private int startlives;
    private int lives;

    public TDGame(String name, String level, ArrayList<Player> players) {
        this.name = name;

        ConfigurationSection configSection = data.getConfigurationSection(level);
        this.startlives = configSection.getInt("lives");
        this.lives = this.startlives;
        this.waves = configSection.getStringList("waves");

        ConfigurationSection gameConfigSection = gamedata.getConfigurationSection(name);
        this.path = (ArrayList<Location>) gameConfigSection.getList("path");

        for(Player player : players) {
            this.players.add(TowerDefense.getInstance().getPlayerRegistry().registerPlayer(player, this));
        }

        //TODO: DELAY SPAWN OF FIRST WAVE
        activeWave = new TDWave(waves.get(0), this);

        games.add(this);
    }

    public void nextWave() {
        if(waves.size() - 1 == waves.indexOf(activeWave.getName())) {
            delete();
        }
        activeWave = new TDWave(waves.get(waves.indexOf(activeWave.getName()) + 1),this);
    }

    public void damage(TDEnemie enemie) {
        lives -= enemie.getDamage();
        if(lives < 0) {
            //TODO: END GAME
        }
    }

    public void removePlayer(TDPlayer p) {
        players.remove(p);

        if(players.size() <= 0) {
            //TODO: END GAME
        }
    }

    public void delete() {
        waves.clear();
        for(TDEnemie enemie : enemies) {
            enemie.remove();
        }
        enemies.clear();
        for(TDTower tower : towers) {
            tower.remove();
        }
        towers.clear();
        games.remove(this);
    }

    public static void unregisterAll() {
        for(TDGame game : games) {
            game.delete();
        }
    }
}
