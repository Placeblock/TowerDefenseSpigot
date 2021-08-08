package placeblock.towerdefense;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.ArrayList;

@AllArgsConstructor
public class TDPath {

    private final ArrayList<Location> locations;

    public Location getLocation(int index) {
        if(index < 0) return null;
        if(index >= locations.size()) return null;;
        return locations.get(index);
    }
}
