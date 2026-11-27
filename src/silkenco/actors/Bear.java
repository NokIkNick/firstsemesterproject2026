package silkenco.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import itumulator.world.Location;
import itumulator.world.World;
import silkenco.enums.AnimalState;

public abstract class Bear extends Animal{

    protected Random random = new Random();
    private Territory territory;

    //max age measured in in-game days
    private static final int MAX_AGE_DAYS = 20;

    public Bear(int age){
        super(100, 100, MAX_AGE_DAYS * World.getTotalDayDuration(), age, 100);
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }

    @Override 
    public void act(World world){
        this.age++;
        
        if(this.hunger <= 0 || this.age > this.maxAge){
            this.die(world);
            return;
        }

        this.hunger--;

        if(world.isNight() && currentState != AnimalState.SLEEPING){
            prevState = currentState;
            currentState = AnimalState.SLEEPING;
        }

        if(world.isDay() && currentState == AnimalState.SLEEPING){
            currentState = prevState;
            prevState = currentState;
        }

        if(currentState == AnimalState.SLEEPING){
            this.sleep();
            return;
        }

        //only reached if the bear is alive and awake
        this.behave(world);
    }

    /**
     * What the bear does on a normal step. Subclasses override this instead of act(),
     * so the upkeep in act() (aging, hunger, dying, sleeping) always runs first.
     * Default: wander around the territory.
     * @param world
     */
    protected void behave(World world){
        this.move(world);
    }

    @Override
    public void move(World world){
        List<Location> territoryTiles = new ArrayList<>(this.territory.getTerritoryLocations());
        List<Location> surroundingLocations = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        List<Location> movableTiles = new ArrayList<>();

        //Danner fællesmængde
        for (Location location : surroundingLocations) {
            if(territoryTiles.contains(location)){
                movableTiles.add(location);
            }
        }

        //Ingen tiles at flytte sig på.
        if(movableTiles.isEmpty()) return;

        world.move(this, movableTiles.get(random.nextInt(movableTiles.size())));
        
    }

    @Override 
    public abstract void die(World world);
}
