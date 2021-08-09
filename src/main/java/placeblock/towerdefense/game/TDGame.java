package placeblock.towerdefense.game;

import lombok.Getter;
import org.bukkit.entity.Player;
import placeblock.towerdefense.TDPath;
import placeblock.towerdefense.TowerDefense;

import java.util.ArrayList;

public class TDGame {

    @Getter private final ArrayList<TDPlayer> players = new ArrayList<>();
    @Getter private final ArrayList<TDTower> towers = new ArrayList<>();
    @Getter private final ArrayList<TDEnemie> enemies = new ArrayList<>();

    private final ArrayList<String> waves;
    @Getter private final TDPath path;
    @Getter private TDWave activeWave;
    private int lives;

    public TDGame(ArrayList<Player> players, TDPath path, ArrayList<String> waves, int lives) {
        this.path = path;
        this.waves = waves;
        this.lives = lives;
        for(Player player : players) {
            this.players.add(TowerDefense.getInstance().getPlayerRegistry().registerPlayer(player, this));
        }

        //TODO: DELAY SPAWN OF FIRST WAVE
        activeWave = new TDWave(waves.get(0), this);
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

}
