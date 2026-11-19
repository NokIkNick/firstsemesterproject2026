package silkenco.actors;



import itumulator.world.Location;
import itumulator.world.World;
import silkenco.enums.RabbitState;

public class BabyRabbit extends Rabbit{
    public BabyRabbit(){
        super(0);
    }

    @Override 
    public void act(World world){
        if(super.getAge() == 10 && super.currentState == RabbitState.Roaming){
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
