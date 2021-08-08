package placeblock.towerdefense.instances;

import org.bukkit.entity.Player;
import placeblock.towerdefense.TDPath;
import placeblock.towerdefense.TowerDefense;
import placeblock.towerdefense.creators.TDWave;

import java.util.ArrayList;

public class TDGameInstance {

    private final ArrayList<TDPlayer> players = new ArrayList<>();
    private final ArrayList<TDWave> waves;
    private final TDPath path;
    private TDWaveInstance activeWave;

    public TDGameInstance(ArrayList<Player> players, TDPath path, ArrayList<TDWave> waves) {
        this.path = path;
        this.waves = waves;
        for(Player player : players) {
            this.players.add(TowerDefense.getInstance().getPlayerRegistry().registerPlayer(player, this));
        }

        //TODO: DELAY SPAWN OF FIRST WAVE
        activeWave = waves.get(0).getInstance(this);
    }

    public void removePlayer(TDPlayer p) {
        players.remove(p);
    }

}
