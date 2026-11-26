package silkenco.actors;

import java.util.List;
import java.util.Set;

import itumulator.world.World;
import silkenco.enums.AnimalState;
import silkenco.util.Locator;

public class AdultBear extends Bear implements IBreedable<AdultBear>{
    
    private List<Class<? extends Animal>> huntingTargets = List.of(Rabbit.class, Wolf.class);
    private Animal currentTarget = null;

    public AdultBear(){
        super(1);
    }

    public void huntTarget(World world){

    }    

    public void locateTarget(Set<Animal> prey,World world){
        this.currentTarget = Locator.locateClosestFromCollection(world, world.getLocation(this), prey);
        
        if(currentTarget == null) return;

        super.setCurrentState(AnimalState.HUNTING);
    }

    public Set<Animal> scanTerritoryForNewTarget(World world){
        return super.getTerritory().scanTerritory(world, this.huntingTargets);
    }


    @Override 
    public void act(World world){

        //we dont exist anymore.
        if(!world.contains(this)) return;

        if(super.getCurrentState() == AnimalState.ROAMING){
            Set<Animal> found = scanTerritoryForNewTarget(world);
            if(!found.isEmpty()){
                locateTarget(found,world);
                return;
            };
        }

        if(super.getCurrentState() == AnimalState.HUNTING && world.isOnTile(currentTarget) && currentTarget.isAlive()){
            

            moveTowards(world.getLocation(this.currentTarget), world);
            return;
        }

        //AWAY FROM TERRITORY WITHOUT TARGET 
        if(super.getCurrentState() == AnimalState.HUNTING && !super.getTerritory().isInTerritory(world, this) && this.currentTarget == null){

        }
        
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
