package placeblock.towerdefense;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import placeblock.towerdefense.commands.TDCommand;
import placeblock.towerdefense.events.*;
import placeblock.towerdefense.events.custom.PlayerInteractTowerEvent;
import placeblock.towerdefense.game.*;
import placeblock.towerdefense.registry.TDPlayerRegistry;

public final class TowerDefense extends JavaPlugin {

    @Getter private static TowerDefense instance;
    @Getter private TDPlayerRegistry playerRegistry;

    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();

        playerRegistry = new TDPlayerRegistry();

        TDCommand command = new TDCommand();
        command.registerCommands();
        getCommand("towerdefense").setExecutor(command);

        Bukkit.getPluginManager().registerEvents(new PlayerInventoryEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerScroll(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPlaceBlock(), this);


        TDEnemie.saveConfig();
        TDTower.saveConfig();
        TDGame.saveConfig();
        TDWave.saveConfig();

        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.HIGHEST,
                        PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() ==
                                PacketType.Play.Client.USE_ENTITY) {
                            Player p = event.getPlayer();
                            TDPlayer player = playerRegistry.getPlayer(p);
                            if(player == null) return;
                            int entityid = event.getPacket().getIntegers().read(0);
                            for(TDGame game : TDGame.getGames()) {
                                for(TDTower tower : game.getTowers()) {
                                    if(tower.getEntity().getId() == entityid) {
                                        PlayerInteractTowerEvent newevent = new PlayerInteractTowerEvent(player, tower);
                                        Bukkit.getPluginManager().callEvent(newevent);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onDisable() {
        TDGame.unregisterAll();
    }
}
