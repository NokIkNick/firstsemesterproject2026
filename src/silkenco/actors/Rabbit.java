package silkenco.actors;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import silkenco.enums.AnimalState;
import silkenco.nonblocking.Entrancehole;
import silkenco.nonblocking.Rabbithole;
import silkenco.util.Locator;


/**
 * Rabbit
 */
public abstract class Rabbit extends Animal{

    protected Random random = new Random();
    protected Rabbithole homeHole;
    

    public Rabbit(int age){
        super(100, 100, 60, age, 100);
        this.homeHole = null;
        currentState = AnimalState.ROAMING;
    }

    @Override 
    public void act(World world){
        super.act(world);

        //we dont exist anymore.
        if(!world.contains(this)) return;

        //home has no entrances left, so forget it
        if(homeHole != null && homeHole.getEntrances().isEmpty()){
            homeHole = null;
        }

        //wake up when it is day
        if(currentState == AnimalState.SLEEPING && world.isDay()){
            currentState = AnimalState.HOME;
        }
        if (currentEnergy <= 0 && currentState == AnimalState.ROAMING && homeHole != null){
            goTowardsHome(world);
            if (isOnEntrance(world)){
                enterHome(world);
                currentState = AnimalState.HOME;
            }
            return;
        }

        if (world.isNight() && currentState == AnimalState.HOME){
            sleep();
            currentState = AnimalState.SLEEPING;
            return;
        }

        //Leaves in the morning or makes new hole
        else if (world.isDay() && currentState == AnimalState.HOME) {
            Entrancehole ranEntrance = homeHole.getEntrances().get(random.nextInt(homeHole.getEntrances().size()));
            if (!ranEntrance.isOccupied(world)){
                ranEntrance.leave(this, world);
                currentState = AnimalState.ROAMING;
            }
            else{
                //50% chance to make a new hole and leave through it
                if (random.nextInt(2)==1 && this instanceof AdultRabbit){
                    List<Entrancehole> oldEntrances = homeHole.getEntrances();
                    //make random entrance
                    homeHole.makeRanEntrance(ranEntrance.getlocation(), world, 2);
                    List<Entrancehole> newEntrances = homeHole.getEntrances();
                    for (Entrancehole e : oldEntrances) {
                        if (!newEntrances.contains(e)){
                            e.leave(this, world);
                        }
                        
                    }
                    
                }
                else{
                    return;
                }
            }
            
        }
        else if(world.isNight() && currentState == AnimalState.ROAMING && homeHole != null){
            goTowardsHome(world);
            if (isOnEntrance(world)){
                enterHome(world);
                currentState = AnimalState.HOME;
            }
        }
        else if (currentState == AnimalState.ROAMING){
            if(hunger <= 60){
                seekFood(world);
            }else{
                this.move(world);
            }
        }
        if (currentEnergy>10){ //TODO make proper
            this.useEnergy();
        }

        hunger-=4 ;
    }

    //eats grass if standing on it, otherwise moves towards the nearest grass
    private void seekFood(World world){
        Location here = world.getLocation(this);
        if(world.containsNonBlocking(here)){
            Object object = world.getNonBlocking(here);
            if (object instanceof Grass){
                this.eat((Grass)object, world);
                return;
            }
        }
        Grass nearestGrass = Locator.locateClosest(world, here, Grass.class);
        if(nearestGrass == null){
            //no grass anywhere, just wander
            this.move(world);
            return;
        }
        this.moveTowards(world.getLocation(nearestGrass), world);
    }

    private void eat(Grass grass, World world){
        grass.getEaten(world);
        hunger = 100;
    }

    //enters the home hole through the entrance the rabbit is standing on
    private void enterHome(World world){
        Location here = world.getLocation(this);
        for (Entrancehole e : homeHole.getEntrances()){
            if (e.getlocation().equals(here)){
                homeHole.enter(e, this, world);
                return;
            }
        }
    }

    private boolean isOnEntrance(World world){
            for (Location l:homeHole.getEntranceLocations() ){
                if(world.contains(this)){
                if (l.equals(world.getLocation(this))){
                    return true;
                }

            }
        }
            return false;
    }

    /**
     * Tries to move the Rabbit, by checking the empty surrounding tiles. If they are empty, the Rabbit moves randomly.
     * @param world
     */

    public void sleep(){
        currentEnergy = energy;
    }

    @Override
    protected void move(World world){
        Set<Location> emptyTiles = world.getEmptySurroundingTiles();
        if(emptyTiles.isEmpty()){
            return;
        }
        List<Location> tiles = new ArrayList<>(emptyTiles);
        Location nextLocation = tiles.get(this.random.nextInt(tiles.size()));
        world.move(this, nextLocation);
    }
    //moves towards the closest entrance in homeHole
    private void goTowardsHome(World world){
        

        Entrancehole closest = Locator.locateClosestFromCollection(world, world.getLocation(this), homeHole.getEntrances());
        moveTowards(closest.getlocation(),world);
    }


    private void useEnergy(){
        if (currentEnergy <= 0) {
            throw new RuntimeException("Could not reduce energy, due to energy being too low."); //TODO make proper exception
        }
        currentEnergy = currentEnergy - 10;

    }

    @Override 
    protected void die(World world){
        //a rabbit that dies inside its hole should not keep the hole from decaying
        if(homeHole != null){
            homeHole.removeFromRabbitHole(this);
        }
        world.delete(this);
    }

    public void setHome(Rabbithole r){
        homeHole = r;
    }

    public int getAge(){
        return this.age;
    }

    public int getMaxAge(){
        return this.maxAge;
    }

    public void setHunger(int hunger){
        this.hunger = hunger;
    }
}