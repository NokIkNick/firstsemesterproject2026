package silkenco.actors;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import silkenco.enums.RabbitState;
import silkenco.nonblocking.Rabbithole;


public class AdultRabbit extends Rabbit implements IBreedable<AdultRabbit>{
    private int reproductionTimer;
    private int detectionRange;
    private int diggingCooldown;

    public AdultRabbit(){
        super(1);
        this.reproductionTimer = 0;
        this.detectionRange = 1;
        this.diggingCooldown = 0;
    }


    @Override
    public void act(World world){ 
        if(this.diggingCooldown == 0 && super.homeHole == null){
            this.dig(world);
        }  

        super.act(world);

        // The rabbit may have died in super.act(); a dead rabbit must not keep acting.
        if(!world.contains(this)){
            return;
        }

        if(reproductionTimer == 0 && super.currentState == RabbitState.Roaming && world.isOnTile(this)){
            AdultRabbit chosenMate = detectMate(world);

            if(chosenMate != null){
                this.reproduce(world, chosenMate);
            }
        }



        if(this.reproductionTimer > 0){
            this.reproductionTimer--;
        }

        if(this.diggingCooldown > 0){
            this.diggingCooldown--;
        }

        
    }

    
    private void dig(World world){
        Location location = world.getLocation(this);
        if(world.containsNonBlocking(location)){
            world.delete(world.getNonBlocking(location));
        }
        
        Rabbithole hole = new Rabbithole();
        super.homeHole = hole;
        hole.makeEntrance(location, world);
        this.diggingCooldown = 20;

    }

    /**
     * Attempts to reproduce with the located mate. If successfull it creates a new BabyRabbit object at one of the empty surrounding tiles and resets both mating Rabbits' reproduction timers.
     * If there is no empty surrounding tile, the reproduction will be halted, and the timers won't be reset.
     * @param world
     * @param chosenMate
     */
    public void reproduce(World world, AdultRabbit chosenMate){
        Set<Location> emptyLocationsSet = world.getEmptySurroundingTiles(world.getLocation(this));
        if(emptyLocationsSet.isEmpty()){
            //TODO: NO ROOM TO REPRODUCE, HALT.
            return;
        }
        
        List<Location> emptyLocations = new ArrayList<>(emptyLocationsSet);
        BabyRabbit baby = new BabyRabbit();
        world.setTile(emptyLocations.get(super.random.nextInt(emptyLocationsSet.size())), baby);
        chosenMate.resetReproductionTimer();
        this.resetReproductionTimer();
        baby.setHome(homeHole);

    }

    /**
     * Attempts to detect a mate, in the area of the detectionRange field. 
     * @param world
     * @return The mate that the Rabbit aims to reproduce with, in the form of an AdultRabbit object.
     */
    public AdultRabbit detectMate(World world){
        Set<Location> surroundingTiles = world.getSurroundingTiles(world.getLocation(this), this.detectionRange);
        Set<AdultRabbit> surroundingRabbits = world.getAll(AdultRabbit.class, surroundingTiles);
        List<AdultRabbit> listOfRabbits = new ArrayList<>();
        for (AdultRabbit r : surroundingRabbits) {
            if (r.getReproductionTimer() == 0) {
                listOfRabbits.add(r);
            }
        }
        if (listOfRabbits.isEmpty()) {
            return null;
        }
        return listOfRabbits.get(super.random.nextInt(listOfRabbits.size()));
    }

    public int getReproductionTimer(){
        return this.reproductionTimer;
    }

    public void resetReproductionTimer(){
        this.reproductionTimer = 15;
    }

}
