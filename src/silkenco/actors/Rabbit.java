package silkenco.actors;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import silkenco.enums.RabbitState;
import silkenco.nonblocking.Entrancehole;
import silkenco.nonblocking.Rabbithole;
import silkenco.util.Locator;
//TODO, implement energy, and foodseeking.
/**
 * Rabbit
 */
public abstract class Rabbit extends Animal{

    protected Random random = new Random();
    protected Rabbithole homeHole;
    protected RabbitState currentState;

    public Rabbit(int age){
        super(100, 100, 60, age);
        this.homeHole = null;
        currentState = RabbitState.Roaming;
    }

    @Override 
    public void act(World world){

        if(this.hunger <= 0 || this.age > this.maxAge){
            this.die(world);
            return;
        }

        this.age++;
        //wake up when it is day
        if(currentState == RabbitState.Sleeping && world.isDay()){
            currentState = RabbitState.Home;
        }
        if (currentEnergy <= 0 && currentState == RabbitState.Roaming){
            goTowardsHome(world);
            if (isOnEntrance(world)){
                    homeHole.enter(homeHole.getEntrances().get(0), this, world);
                    currentState = RabbitState.Home;
            }
            return;
        }
        //Hunger check MÅSKE VI SKAL SE PÅ AT LAVE BETINGELSERNE OM
        if(hunger<=60 && currentState == RabbitState.Roaming){
            if(world.containsNonBlocking(world.getLocation(this))){
                Object object = world.getNonBlocking(world.getLocation(this));
                if (object instanceof Grass){
                    this.eat((Grass)object, world);
                    return;
                }
            }
                Grass nearestGrass = Locator.locateClosest(world,world.getLocation(this), Grass.class);
            if(world.getLocation(this).equals(world.getLocation(nearestGrass))){
                this.eat(nearestGrass, world);
            }else{
                this.moveTowards(world.getLocation(nearestGrass), world);
            }
        
        }

        if (world.isNight() && currentState == RabbitState.Home){
                sleep();
                currentState = RabbitState.Sleeping;
                return;
        }
        else if (world.isDay() && currentState == RabbitState.Home) {
            Entrancehole ranEntrance = homeHole.getEntrances().get(random.nextInt(homeHole.getEntrances().size()));
            try{ranEntrance.leave(this, world);}
            catch(Exception e){
                return; //TODO VERY TEMP FIX!!!
            }
            currentState = RabbitState.Roaming;
        }
        else if(world.isNight() && currentState == RabbitState.Roaming){ 
            goTowardsHome(world);//TODO throws null pointer when no home hole is present
                if (isOnEntrance(world)){
                    homeHole.enter(homeHole.getEntrances().get(0), this, world); //TODO should NOT only use the first hole, should check which hole rabbit is on first and use that with aiding method.
                    currentState = RabbitState.Home;
        }
    }
        else if (currentState == RabbitState.Roaming){
            this.move(world);
        }
        if (currentEnergy>10){ //TODO make proper
            this.useEnergy();
        }

        hunger-=4 ; 

        
    }

    private void eat(Grass grass, World world){
        grass.getEaten(world);
        hunger = 100;
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

    //TODO: GAMMEL METODE, SKAL UD.
    // private void makehome(World world){
    //     Rabbithole home = new Rabbithole();
    //     homeHole = home;
    //     home.makeEntrance(world.getLocation(this), world);
    // }





    /**
     * Tries to move the Rabbit, by checking the empty surrounding tiles. If they are empty, the Rabbit moves randomly.
     * @param world
     */
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

    //TODO: GAMMEL METODE
    // private void moveTowards(Location location,World world){ //TODO throws an exception
    //     int LocationX = location.getX();
    //     int LocationY = location.getY();
    //     int thisLocationX = world.getLocation(this).getX();
    //     int thisLocationY = world.getLocation(this).getY();
    //     Location newloc;
    //     if (!(LocationX - thisLocationX == 0) && !(LocationY - thisLocationY == 0)){
    //         //move diagonally
    //         newloc = new Location(thisLocationX + toNearestOne(LocationX - thisLocationX),thisLocationY + toNearestOne(LocationY - thisLocationY));
    //         world.move(this, newloc);
    //     }
    //     else if(!(LocationX - thisLocationX == 0)){
    //             //move towards X
    //             newloc = new Location(thisLocationX + toNearestOne(LocationX - thisLocationX),thisLocationY);
    //             world.move(this, newloc);

    //     }
    //     else if(!(LocationY - thisLocationY == 0)){
    //             //Move towards Y
    //             newloc = new Location(thisLocationX,thisLocationY + toNearestOne(LocationY - thisLocationY));
    //             world.move(this, newloc);
    //     }
    //     else{
    //         //Already at target!!!
    //     }
    // }

    //TODO FLYTTET TIL LOCATOR
    // private int toNearestOne(int number){
    //     if (number<0){
    //         return -1;
    //     }
    //     else if(number>0){
    //         return 1;
    //     }
    //     else{
    //         return 0;
    //     }
    // }


    private void useEnergy(){
        if (currentEnergy <= 0) {
            throw new RuntimeException("Could not reduce energy, due to energy being too low."); //TODO make proper exception
        }
        currentEnergy = currentEnergy - 10;

    }

    @Override 
    protected void die(World world){
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