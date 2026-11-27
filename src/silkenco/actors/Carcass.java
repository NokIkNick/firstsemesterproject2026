package silkenco.actors;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Carcass implements Actor{
    int decayTimer;
    boolean hasMushroom;
    //Size should be between 10 and 100 depending on the animals size roughly.
    int size;
    Carcass(int size, Boolean hasMushroom){
       decayTimer = 60; 
       this.hasMushroom = hasMushroom;
       this.size = size;
    }

    @Override
    public void act(World world) {
        doDecay();
        if (decayTimer <= 0){
            decay(world);
        }
    }

    public void infect(){
        hasMushroom = true;
    }

    private void doDecay(){
        decayTimer--;
        if (hasMushroom){
            decayTimer -= 2;
        }
    }

    private void decay(World world){
        Location loc = world.getLocation(this);
        world.delete(this);
        if (hasMushroom){
            world.setTile(loc, new Mushroom());
        }
        
        
    }

    public int getSize(){
        return size;
    }

    public boolean isInfected(){
        return hasMushroom;
    }

    
}
