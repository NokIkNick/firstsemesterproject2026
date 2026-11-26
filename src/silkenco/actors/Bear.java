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

    public Bear(int age){
        super(100, 100, 60, age, 100);
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
