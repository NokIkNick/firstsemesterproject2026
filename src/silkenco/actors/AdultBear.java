package silkenco.actors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;
import silkenco.enums.AnimalState;
import silkenco.util.Locator;

public class AdultBear extends Bear implements IBreedable<AdultBear>{
    
    private List<Class<? extends Animal>> huntingTargets = List.of(Rabbit.class, Wolf.class);
    private Animal currentTarget = null;
    private static final int HUNGER_THRESHOLD = 20;

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

    /**
     * Searches the whole world for prey.
     * Finds the closest animal of each hunting target type, and then hunts the closest of those.
     * @param world
     * @return true if a target was found, false if there is no prey anywhere.
     */
    public boolean searchOutsideTerritory(World world){
        Location here = world.getLocation(this);
        Set<Animal> candidates = new HashSet<>();
        for (Class<? extends Animal> preyClass : this.huntingTargets) {
            Animal closest = Locator.locateClosest(world, here, preyClass);
            if(closest != null){
                candidates.add(closest);
            }
        }

        if(candidates.isEmpty()) return false;

        locateTarget(candidates, world);
        return true;
    }

    /**    
     * 
     * @see silkenco.actors.Bear#behave(itumulator.world.World)
     */
    @Override
    protected void behave(World world){

        //FREE ROAMING IN TERRITORY
        if(super.getCurrentState() == AnimalState.ROAMING){
            Set<Animal> found = scanTerritoryForNewTarget(world);
            if(!found.isEmpty()){
                locateTarget(found,world);
                return;
            };

            //LOOK OUTSIDE OF TERRITORY FOR FOOD WHEN TOO HUNGRY
            if(hunger <= HUNGER_THRESHOLD && searchOutsideTerritory(world)){
                return;
            }
        }

        //HUNTING STATE
        if(super.getCurrentState() == AnimalState.HUNTING){
            //isOnTile throws if the target was deleted, so check contains first
            boolean targetValid = currentTarget != null  && world.contains(currentTarget) && world.isOnTile(currentTarget) && currentTarget.isAlive();

            if(targetValid){
                moveTowards(world.getLocation(this.currentTarget), world);
                if(world.getSurroundingTiles(world.getLocation(this), 1).contains(world.getLocation(this.currentTarget))){
                    this.currentTarget.die(world);
                    this.hunger = 100;
                }
                return;
            }

            //Target got away or died
            this.currentTarget = null;
            headTowardsHome(world);
            return;
        }

        //ON ITS WAY HOME
        if(super.getCurrentState() == AnimalState.HOMEBOUND){
            if(super.getTerritory().isInTerritory(world, this)){
                this.currentState = AnimalState.ROAMING;
            }else {
                headTowardsHome(world);
                return;
            }
        }

        //nothing else to do, so wander around the territory
        this.move(world);
    }

    private void headTowardsHome(World world){
        if(this.currentState != AnimalState.HOMEBOUND){
            this.prevState = this.currentState;
            this.currentState = AnimalState.HOMEBOUND;
        }
        
        super.moveTowards(super.getTerritory().getCenter(), world);
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
