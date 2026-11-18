package silkenco.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    //TODO: add home field later.

    public Rabbit(int age){
        this.hunger = 100;
        this.age = age;
        this.energy = 100;
        this.maxAge = 60;
    }

    @Override 
    public void act(World world){
        this.move(world);
        
        if(this.hunger <= 0 || this.age > this.maxAge){
            this.die(world);
        }

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