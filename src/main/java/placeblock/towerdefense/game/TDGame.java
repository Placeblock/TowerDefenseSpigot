package placeblock.towerdefense.game;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import placeblock.towerdefense.TowerDefense;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TDGame implements Listener {
    @Getter private final static ArrayList<TDGame> games = new ArrayList<>();

    private final static File file = new File(TowerDefense.getInstance().getDataFolder() + "/levels.yml");
    private final static YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
    private final static File gamefile = new File(TowerDefense.getInstance().getDataFolder() + "/games.yml");
    private final static YamlConfiguration gamedata = YamlConfiguration.loadConfiguration(gamefile);

    @Getter private final String name;

    @Getter private final KeyedBossBar bossBar;

    @Getter private final ArrayList<TDPlayer> players = new ArrayList<>();
    @Getter private final ArrayList<TDTower> towers = new ArrayList<>();

    @Getter private final List<String> allowedtowers;

    @Getter private final ArrayList<Location> path;

    private final List<String> waves;
    @Getter private final ArrayList<TDWave> activeWaves = new ArrayList<>();
    private int lastWave = 0;

    private int startlives;
    private int lives;

    public TDGame(String name, String level, ArrayList<Player> players) {
        this.name = name;


        ConfigurationSection configSection = data.getConfigurationSection(level);
        this.startlives = configSection.getInt("lives");
        this.lives = this.startlives;
        this.waves = configSection.getStringList("waves");

        this.allowedtowers = configSection.getStringList("allowedtowers");

        this.bossBar = Bukkit.createBossBar(new NamespacedKey(TowerDefense.getInstance(), "bb" + name), ChatColor.GREEN + "Leben: " + lives + "/" + startlives, BarColor.BLUE, BarStyle.SOLID);
        this.bossBar.setProgress(lives/startlives);


        ConfigurationSection gameConfigSection = gamedata.getConfigurationSection(name);
        this.path = (ArrayList<Location>) gameConfigSection.getList("path");

        for(Player player : players) {
            this.players.add(TowerDefense.getInstance().getPlayerRegistry().registerPlayer(player, this));
            this.bossBar.addPlayer(player);
        }

        //TODO: DELAY SPAWN OF FIRST WAVE
        activeWaves.add(new TDWave(waves.get(0), this));

        games.add(this);
    }

    public ArrayList<TDEnemie> getEnemies() {
        ArrayList<TDEnemie> enemies = new ArrayList<>();
        for(TDWave wave : this.activeWaves) {
            for(TDEnemie enemie : wave.getEnemies()) {
                enemies.add(enemie);
            }
        }
        return enemies;
    }

    public void nextWave() {
        lastWave++;
        if(lastWave >= waves.size()) {
            delete();
            return;
        }
        activeWaves.add(new TDWave(waves.get(lastWave),this));
    }

    public void damage(TDEnemie enemie) {
        lives -= enemie.getDamage();
        if(lives < 0) lives = 0;
        if(lives <= 0) {
            delete();
        }
        this.bossBar.setProgress(lives/ (double) startlives);
        this.bossBar.setTitle(ChatColor.GREEN + "Leben: " + lives + "/" + startlives);
    }

    public void removePlayer(TDPlayer p) {
        players.remove(p);

        if(players.size() <= 0) {
            delete();
        }
    }

    public void delete() {
        waves.clear();
        for(TDWave activeWave : activeWaves) {
            activeWave.delete();
        }
        activeWaves.clear();
        for(TDTower tower : towers) {
            tower.remove();
        }
        towers.clear();
        for(TDPlayer p : players) {
            TowerDefense.getInstance().getPlayerRegistry().unregisterPlayer(p.getP());
            p.remove();
        }
        players.clear();
        bossBar.removeAll();
        Bukkit.removeBossBar(bossBar.getKey());
    }

    public static void unregisterAll() {
        for(TDGame game : games) {
            game.delete();
        }
        games.clear();
    }

    public static void addWaypoint(String name, Location location) {
        List<Location> locations = new ArrayList<>();
        if(!gamedata.getConfigurationSection(name).contains("path")) {
            gamedata.getConfigurationSection(name).createSection("path");
        }else {
            locations = (List<Location>) gamedata.getList(name + ".path");
        }
        locations.add(location);
        gamedata.set(name + ".path", locations);
        saveConfig();
    }

    public static void saveConfig() {
        try {
            data.save(file);
            gamedata.save(gamefile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
