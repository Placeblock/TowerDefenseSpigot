package placeblock.towerdefense.instances;

import lombok.Getter;
import org.bukkit.entity.Player;
import placeblock.towerdefense.TDPath;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDWave;

import java.util.ArrayList;

public class TDGameInstance {

    private final ArrayList<TDPlayer> players = new ArrayList<>();
    private final ArrayList<TDTowerInstance> towers = new ArrayList<>();

    private final ArrayList<TDWave> waves;
    @Getter private final TDPath path;
    @Getter private TDWaveInstance activeWave;
    private int lives;

    public TDGameInstance(ArrayList<Player> players, TDPath path, ArrayList<TDWave> waves, int lives) {
        this.path = path;
        this.waves = waves;
        this.lives = lives;
        for(Player player : players) {
            this.players.add(TowerDefense.getInstance().getPlayerRegistry().registerPlayer(player, this));
        }

        //TODO: DELAY SPAWN OF FIRST WAVE
        activeWave = waves.get(0).getInstance(this);
    }

    public void damage(TDEnemieInstance enemie) {
        lives -= enemie.getEnemie().getDamage();
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
