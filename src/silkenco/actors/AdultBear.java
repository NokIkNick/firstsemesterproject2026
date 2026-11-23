package silkenco.actors;

import itumulator.world.World;

public class AdultBear extends Bear implements IBreedable<AdultBear>{
    
    public AdultBear(){
        super(1);
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
