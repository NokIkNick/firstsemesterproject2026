package silkenco.nonblocking;

import itumulator.simulator.*;
import itumulator.world.*;

public class Entrancehole implements NonBlocking{
    private Location location;
    //The hole which the Entrancehole belongs to
    private Rabbithole hole;
    //deterioration timer
    int time;
    public Entrancehole(Location location, Rabbithole hole){
        this.location=location;
        this.hole=hole;
        time = 0;
    }


    //adds an actor to the hole and removes them from world.
    public void Enter(Actor actor, World world){
        resetTimer();
        hole.addToRabbitHole(actor);
        world.remove(actor);
    }

    public void leave(Actor actor, World world){
        world.setTile(location, actor);
        resetTimer();
        hole.removeFromRabbitHole(actor);
        
        
    }

    public Location getlocation(){
        return location;
    }

    public void doDeterioration(World world){
        time++;
        int timer = 20;
        if (time >= timer){
            world.delete(this);
        }
    }

    private void resetTimer(){
        time = 0;
    }
}