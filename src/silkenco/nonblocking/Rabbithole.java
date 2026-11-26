package silkenco.nonblocking;
import itumulator.simulator.*;
import itumulator.world.*;
import java.util.*;
import silkenco.actors.Grass;

public class Rabbithole implements NonBlocking{
    private Boolean hasTunnel;
    //possible name change, as it is a list of all Actors in hole, currently just rabbits
    private List<Actor> Rabbits;
    private List<Entrancehole> entrances;

    public Rabbithole(){
        hasTunnel = false;
        Rabbits = new ArrayList<>();
        entrances = new ArrayList<>();
    }

    public void makeEntrance(Location location, World world){
        if(world.containsNonBlocking(location)){
            Object object = world.getNonBlocking(location);
                if(!(object instanceof Grass)){
                    //cant dig on top of an entrance (or anything else that isnt grass)
                 return;
                }
            world.delete(object);
        }
        Entrancehole e = new Entrancehole (location,this);
        world.setTile(location, e);
        entrances.add(e);
        tunnelCheck();
    }

    public void makeRanEntrance(Location location, World world, int range){
        Random random = new Random();
        while (true) { 
            int locationx = location.getX();
            int locationy = location.getY();
            int movex = random.nextInt(range);
            int movey = random.nextInt(range);
            //50% chance to add or subtract range for x and y.
            if (random.nextBoolean()){
                locationx = locationx-movex;
            }
            else{
                locationx = locationx+movex;
            }
            if (random.nextBoolean()){
                locationy = locationy-movey;
            }
            else{
                locationy = locationy+movey;
            }
            //if location is not out of bounds
            if (!(world.getSize()<locationx||world.getSize()<locationy||locationy<0||locationx<0)){
                //makes the new hole
                Location holeLoc = new Location(locationx,locationy);
                makeEntrance(holeLoc, world);
                break;
            }
        }
    }

    public void removeEntrance(Entrancehole e){
        entrances.remove(e);
        tunnelCheck();
    }

    public List<Location> getEntranceLocations(){
        List<Location> entranceLocations = new ArrayList<>();
        for (Entrancehole e : entrances) {
            entranceLocations.add(e.getlocation());
        }

        return entranceLocations;
    }

    public void addToRabbitHole(Actor actor){
        Rabbits.add(actor);
    }

    public void removeFromRabbitHole(Actor actor){
        Rabbits.remove(actor);
    }

    private void tunnelCheck(){
        if (entrances.size() >= 2){
            hasTunnel = true;
        }
        else {
            hasTunnel = false;
        }
    }

    //checks if the EntranceHole is in the list of entrances, then leaves
    public void leave(Entrancehole exit, Actor actor, World world) throws Exception{
        if (!entrances.contains(exit)){
            throw new Exception("Tried to leave through a hole that was not connected to the tunnel!");
        }
        exit.leave(actor, world);

    }
    public void enter(Entrancehole entrance, Actor actor, World world){
        entrance.Enter(actor, world);
    }


    //getters
    public List<Entrancehole> getEntrances(){
        return entrances;
    }

    public List<Actor> getRabbits(){
        return Rabbits;
    }

    public boolean hasTunnel(){
        return hasTunnel;
    }

}
