package silkenco.actors;
import itumulator.world.Location;
import itumulator.world.World;

import silkenco.nonblocking.WolfDen;
import silkenco.util.Locator;
import java.util.List;
import java.util.Random;
import silkenco.enums.AnimalState;

//Flyttet fra Rabbit animal: sleep(), useEnergy(), setHome(), getAge(), getMaxAge(), setHunger();
//TODO hunt, die, spawn, spis, find flok, lav hule, gå ind i hule, reproduce (voksen), slås(voksen)

public abstract class Wolf extends Animal{
    protected Random random = new Random();
    protected WolfDen home; 
    private boolean isHome; 
    protected WolfPack pack;
    
    //Husk at have pack i konstruktør i baby og voksen
    public Wolf(int age, WolfPack pack){
        super(100, 100, 50, age, 100);
        currentState = AnimalState.ROAMING; 
        this.home = null;
        isHome = false; 
        this.pack = pack;
        pack.addWolf(this);
    }

    @Override
    public void act(World world){
        super.act(world);
        
         //if (currentEnergy <= 0 && currentState == AnimalState.ROAMING){
           // goTowardsHome(world);
           // if (isOnEntrance(world)){
          //          homeHole.enter(homeHole.getEntrances().get(0), this, world);
         //   }
          //  return;

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
        
        //LAV EN ALFA
        //for der er vel en mulighed for at de deler sig op i minigrupper alt efter hvor de spawner
        
}
 //TODO Opdater med Sofus' patch
    //private void goTowardsHome(World world){
      //  WolfDen closest = Locator.locateClosest(world, world.getLocation(this), WolfDen.class);
      //  moveTowards(closest.getlocation(),world);
  //  }
}
