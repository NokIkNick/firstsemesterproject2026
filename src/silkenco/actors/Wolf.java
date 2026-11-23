package silkenco.actors;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import silkenco.nonblocking.WolfDen;
import silkenco.enums.WolfState;
import silkenco.util.Locator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

//TODO hunt, die, spawn, spis, find flok, lav hule, gå ind i hule, reproduce (voksen), slås(voksen)
public abstract class Wolf extends Animal{
    protected Random random = new Random();
    protected WolfState currentState; 
    protected WolfDen home; 
    private boolean isHome; 
    protected WolfPack pack;
    private int health;

    //Husk at have pack i konstruktør i baby og voksen
    public Wolf(int age, WolfPack pack, int health){
        super(100, 100, 50, age);
        currentState = WolfState.Roaming; 
        this.home = null;
        isHome = false; 
        this.pack = pack;
        pack.addWolf(this);
        this.health = health;
    }

    @Override
    public void act(World world){

        if(this.hunger <= 0 || this.age > this.maxAge || this.health <= 0){
            this.die(world);
            return;
        }

        this.age++;
        
        if(pack.getTarget() != null){
            hunt(world);
        }else{
            followPack(world);
        }

        if(hunger<=40){
            this.hunt(world);
        }
        
    }

    public void hunt(World world){
        Rabbit target = pack.getTarget();
        if(target == null){
            Rabbit rabbit = Locator.locateClosest(world, world.getLocation(this), Rabbit.class);
            pack.chooseTarget(rabbit);
            //FInd kanin tæt på og sæt som mål
            
            if(rabbit!= null){
                pack.chooseTarget(rabbit);
            }

            return;
        }
        // ser om kaninen stadig er i verden
         Location targetLocation = world.getLocation(target);

         if (targetLocation == null) {
             pack.chooseTarget(null);
            return;
        }

    this.moveTowards(targetLocation, world);
    //  NÆSTE TRIN ER SPIS KANIN NÅR DEN STÅR PÅ FLISEN VED SIDEN AF
}
        //flyt denne ulv mod byttet/target
        //hvis ulven er på flise ved siden af:
        //spis kanin

        //men hvad gør de andre ulve imens??? followPack()
    

    public void followPack(World world) {
        List<Wolf> wolves = pack.getMembers();

        Wolf nearestWolf = null; 
        double lowestDist = Double.MAX_VALUE;

        Location origin = world.getLocation(this);

        for(Wolf wolf : wolves){
            //vælg ikke sig selv
            if (wolf != this){

                Location different = world.getLocation(wolf);
                
                if (different == null){
                    continue;
                }

                //afstaandsudregning stjålet fra locatoren... Ved ikke om man kan gøre det smartere (det kan man helt sikkert)
            int xdiff = origin.getX() - different.getX();
            int ydiff = origin.getY() - different.getY();
            double distance = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
            if (distance < lowestDist){
                lowestDist = distance;
                nearestWolf = wolf;
            }

            }
        
        }
        //>1 så ulven kun flytter sig hvis den er længere væk end 1 flise
        if (nearestWolf != null && lowestDist > 1){
                this.moveTowards(world.getLocation(nearestWolf), world);
             }
        
        //evt. lav en slags "floklocation" som er midten af flokken eller noget... 
        //for der er vel en mulighed for at de deler sig op i minigrupper alt efter hvor de spawner
        
}
}
