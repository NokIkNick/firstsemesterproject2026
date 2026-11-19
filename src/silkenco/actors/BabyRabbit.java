package silkenco.actors;



import itumulator.world.Location;
import itumulator.world.World;
import silkenco.nonblocking.Rabbithole;

public class BabyRabbit extends Rabbit{
    private Rabbithole homeHole;
    public BabyRabbit(){
        super(0);
        this.homeHole = null;
    }

    @Override 
    public void act(World world){
        if(super.getAge() == 10){
            this.grow(world);
        }else {
            super.act(world);
        }

    }


    /**
     * Makes the BabyRabbit grow up, saving its location, removing it, then replacing it with an AdultRabbit, finally deleting the BabyRabbit for good. 
     * @param world
     */
    private void grow(World world){ //TODO does not work if rabbit is home
        Location currentLocation = world.getLocation(this);
        world.remove(this);
        world.setTile(currentLocation, new AdultRabbit());
        world.delete(this);
    }
    
}
