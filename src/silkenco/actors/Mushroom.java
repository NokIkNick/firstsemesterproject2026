package silkenco.actors;

import itumulator.simulator.Actor;
import itumulator.world.World;
import java.util.List;
import silkenco.util.Locator;

public class Mushroom implements  Actor{

    int decayTimer;
    int spreadRange;
    Mushroom(){
        decayTimer = 60;
        spreadRange = 2;
    }

    public void act(World world){
        doDecay();
        trySpread(world);
        if(decayTimer <= 0){
            decay(world);
        }
    }


    private void doDecay(){
        decayTimer--;
    }

    private void decay(World world){
        world.delete(this);
    }

    private void trySpread(World world){
        List<Carcass> Carcasses = Locator.LocateAll(world, Carcass.class);
        //Method locate all returns null if there are no carcassses, so a check is in order to return if no carcasses are present.
        if (Carcasses == null){
            return;
        }
        for(Carcass c:Carcasses){
            //if the carcass is within spread range
            if(!(Locator.distanceTo(world, world.getLocation(this), world.getLocation(c))>spreadRange));
                c.infect();
        }
        

    }
}
