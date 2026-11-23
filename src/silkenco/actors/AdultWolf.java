package silkenco.actors;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import silkenco.enums.AnimalState;
import silkenco.actors.*;
import silkenco.nonblocking.Rabbithole;
import silkenco.nonblocking.WolfDen;

public class AdultWolf extends Wolf implements IBreedable<AdultWolf>{
private int reproductionTimer;
private int diggingCooldown;

    public AdultWolf(WolfPack pack){
        super(1, pack);
        this.reproductionTimer = 0;
         this.diggingCooldown = 0;
    }
    @Override
    public void act(World world){
        if(this.diggingCooldown == 0 && super.home == null){
            this.dig(world);
        }  
    }

    public void move(World world){

    }

    public void die(World world){

    }

    private void dig(World world){
        Location location = world.getLocation(this);
        if(world.containsNonBlocking(location)){
            //CANT CREATE HOLE
            return;
        }
        
        WolfDen den = new WolfDen();
        super.home = den;
        den.makeDen(location, world);
        this.diggingCooldown = 20;

    }

    public void reproduce(World world, AdultWolf wolf){

    }


    public AdultWolf detectMate(World world){
         return null;
    }

   

}

