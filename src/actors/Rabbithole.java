package actors;
import itumulator.simulator.*;
import itumulator.world.*;
import java.util.*;

public class Rabbithole implements NonBlocking{
    private Boolean hasTunnel;
    //possible name change, as it is a list of all Actors in hole, currently just rabbits
    private List<Actor> Rabbits;
    private List<Entrancehole> entrances;

    Rabbithole(){
        hasTunnel = false;
        Rabbits = new ArrayList<>();
        entrances = new ArrayList<>();
    }

    public void makeEntrance(Location location, World world){
        Entrancehole e = new Entrancehole (location,this);
        world.setTile(location, e);
        entrances.add(e);
        tunnelCheck();
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
