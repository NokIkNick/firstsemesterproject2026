package actors;

import itumulator.simulator.Actor;
import itumulator.world.*; 
import java.util.*;

public class Grass implements Actor, NonBlocking{
    private int stepCounter = 0; 

    @Override 
    public void act(World world){
       stepCounter++;
       
        if (stepCounter < 20) {
            return; 
        }

        //finder græstots placering
        Location location = world.getLocation(this);

        //findeer ledige felter omkring græstot
        Set<Location> emptyTiles = world.getEmptySurroundingTiles(location);

        //ser om der er én tom naboflise
        if(!emptyTiles.isEmpty()){

            Random random = new Random(); 

            // finder et random tal mellem 0 og antallet af ledige felter (minus 1.)
            int tileCounter = random.nextInt(emptyTiles.size());

            //gennemgår de ledige felter
            for(Location tile : emptyTiles){

                if (tileCounter == 0) {
                    
                     Location newLocation = tile;

                     
                     Grass newGrass = new Grass();


                    world.setTile(newLocation, newGrass);

                }

                tileCounter--;
            } 
            stepCounter = 0; 

        }  
    } 
}
