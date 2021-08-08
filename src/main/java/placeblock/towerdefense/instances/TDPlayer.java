package placeblock.towerdefense.instances;

import lombok.Getter;
import org.bukkit.entity.Player;

public class TDPlayer {

    @Getter private final Player p;
    @Getter private int money = 500;
    @Getter private final TDGameInstance game;

    public TDPlayer(Player p, TDGameInstance game) {
        this.p = p;
        this.game = game;
    }

    public int addMoney(int money) {
        this.money += money;
        return this.money;
    }

    public int removeMoney(int money) {
        this.money -= money;
        return this.money;
    }

}
