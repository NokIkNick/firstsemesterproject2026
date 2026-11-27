package silkenco.actors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

public class Territory {
    
    private Animal owner;
    private Location center;
    private Location upperRightCorner;
    private Location upperLeftCorner;
    private Location lowerRightCorner;
    private Location lowerLeftCorner;
    private Set<Location> territoryLocations = new HashSet<>();

    public Territory(Location center, Animal owner, int range, World world){
        this.owner = owner;
        this.center = center;
        this.upperRightCorner = new Location(center.getX()+range, center.getY()+range);
        this.upperLeftCorner = new Location(center.getX()-range, center.getY()+range);
        this.lowerLeftCorner = new Location(center.getX()-range, center.getY()-range);
        this.lowerRightCorner = new Location(center.getX()+range, center.getY()-range);
        this.territoryLocations = fillTerritorySet(world, range);
    }

    private Set<Location> fillTerritorySet(World world, int range){
        Set<Location> surroundingTerritory = world.getSurroundingTiles(this.center, range);
        surroundingTerritory.add(this.center);
        return surroundingTerritory;
    }

    public Set<Animal> scanTerritory(World world, List<Class<? extends Animal>> prey){
        Set<Animal> found = new HashSet<>();
        for (Class<? extends Animal> preyClass : prey) {
            found.addAll(world.getAll(preyClass, territoryLocations));
        }
        found.remove(owner);
        return found;
    }

    public boolean isInTerritory(World world, Object object){
        return territoryLocations.contains(world.getLocation(object));
    }

    
    public Set<Location> getTerritoryLocations() {
        return territoryLocations;
    }

    public Location getCenter() {
        return center;
    }

    public Location getLowerLeftCorner() {
        return lowerLeftCorner;
    }

    public Location getLowerRightCorner() {
        return lowerRightCorner;
    }

    public Animal getOwner() {
        return owner;
    }

    public Location getUpperLeftCorner() {
        return upperLeftCorner;
    }

    public Location getUpperRightCorner() {
        return upperRightCorner;
    }

    

}
