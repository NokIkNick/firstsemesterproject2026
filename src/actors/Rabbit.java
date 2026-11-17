package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map;

import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;

/**
 * Rabbit
 */
public abstract class Rabbit implements Actor{

    protected Random random = new Random();
    private int hunger;
    private int age;
    private int energy;
    private int maxAge;
    private Rabbithole homeHole;

    public Rabbit(int age){
        this.hunger = 100;
        this.age = age;
        this.energy = 100;
        this.maxAge = 60;
    }

    @Override 
    public void act(World world){
        
        
        if(this.hunger <= 0 || this.age > this.maxAge){
            this.die(world);
            return;
        }

        if (world.isNight()){
            goTowardsHome(world);
        }

        this.move(world);

        this.age++;
    }

    /**
     * Tries to move the Rabbit, by checking the empty surrounding tiles. If they are empty, the Rabbit moves randomly.
     * @param world
     */
    private void move(World world){
        Set<Location> emptyTiles = world.getEmptySurroundingTiles();
        if(emptyTiles.isEmpty()){
            return;
        }
        List<Location> tiles = new ArrayList<Location>(emptyTiles);
        Location nextLocation = tiles.get(this.random.nextInt(tiles.size()));
        world.move(this, nextLocation);
    }
    //moves towards the closest entrance in homeHole
    private void goTowardsHome(World world){
        Entrancehole closest = homeHole.getEntrances().get(0);
        if(homeHole.hasTunnel()){
            //find closest hole
            List<Entrancehole> entrances = homeHole.getEntrances();
            Map<Entrancehole, Double> Distances = new HashMap<>();
            for (Entrancehole e: entrances){
                Location rabbitLocation = world.getLocation(this);
                int xdiff = Math.abs(rabbitLocation.getX() - e.getlocation().getX());
                int ydiff = Math.abs(rabbitLocation.getY() - e.getlocation().getY());
                double distance = Math.sqrt((xdiff^2 * ydiff^2));
                Distances.put(e, distance);
            }   
            double lowestdist = 9999;
            for (Entrancehole e: Distances.keySet()){
                if (lowestdist>Distances.get(e)){
                    lowestdist=Distances.get(e);
                    closest = e;
                }
            }
        }
        moveTowards(closest.getlocation(),world);


    }

    private void moveTowards(Location location,World world){
        int LocationX = location.getX();
        int LocationY = location.getY();
        Location newloc;
        if (!(LocationX - world.getLocation(this).getX() == 0) && !(LocationY - world.getLocation(this).getY() == 0)){
            //move diagonally
            newloc = new Location(toNearestOne(LocationX - world.getLocation(this).getX()),toNearestOne(LocationY - world.getLocation(this).getY()));
            world.move(this, newloc);
        }
        else if(!(LocationX - world.getLocation(this).getX() == 0)){
                //move towards X
                newloc = new Location(toNearestOne(LocationX - world.getLocation(this).getX()),world.getLocation(this).getY());
                world.move(this, newloc);

        }
        else if(!(LocationY - world.getLocation(this).getY() == 0)){
                //Move towards Y
                newloc = new Location(world.getLocation(this).getX(),toNearestOne(LocationY - world.getLocation(this).getY()));
                world.move(this, newloc);
        }
        else{
            //Already at target!!!
        }
    }
    private int toNearestOne(int number){
        if (number<0){
            return -1;
        }
        else if(number>0){
            return 1;
        }
        else{
            return 0;
        }
    }


    private void die(World world){
        world.delete(this);
    }

    public int getAge(){
        return this.age;
    }

    public int getMaxAge(){
        return this.maxAge;
    }
}