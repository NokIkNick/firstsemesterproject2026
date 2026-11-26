package silkenco.actors;

import java.util.List;

import itumulator.world.World;

public class AdultBear extends Bear implements IBreedable<AdultBear>{
    
    private List<Class<? extends Animal>> huntingTargets = List.of(Rabbit.class, Wolf.class);
    private Animal currentTarget = null;

    public AdultBear(){
        super(1);
    }

    public void huntTarget(World world){

   }

   public void locateTarget(World world){

   }

   public void findNewTarget(World world){
        
   }


    @Override 
    public void act(World world){
        super.act(world);
    }

    @Override
    public AdultBear detectMate(World world) {
        throw new UnsupportedOperationException("Unimplemented method 'detectMate'");
    }

    @Override
    public void reproduce(World world, AdultBear chosenMate) {
        throw new UnsupportedOperationException("Unimplemented method 'reproduce'");
    }

    @Override
    public void die(World world) {
        world.delete(this);
    }

}
