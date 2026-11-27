package silkenco.util;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import silkenco.actors.AdultBear;
import silkenco.actors.AdultRabbit;
import silkenco.actors.AdultWolf;
import silkenco.actors.BabyRabbit;
import silkenco.actors.Bear;
import silkenco.actors.Carcass;
import silkenco.actors.Grass;
import silkenco.actors.Mushroom;
import silkenco.actors.Territory;
import silkenco.actors.WolfPack;
import silkenco.nonblocking.Entrancehole;
import silkenco.nonblocking.Rabbithole;
import silkenco.util.io.SpawnInfo;
import silkenco.util.io.WorldInfo;

public class WorldBuilder {

    private Random random = new Random();

    /***
     * Instantiates a Program object, and configurates it.
     * @param size the size of the world.
     * @param displaySize the size of the display in pixels
     * @param delay the deplay in ms
     * @return A configurated program.
     */
    public Program buildProgram(int size, int displaySize, int delay){
        Program program = new Program(size, displaySize, delay);
        program.setDisplayInformation(AdultRabbit.class, new DisplayInformation(Color.darkGray, "rabbit-large"));
        program.setDisplayInformation(BabyRabbit.class, new DisplayInformation(Color.red, "rabbit-small"));
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Entrancehole.class, new DisplayInformation(Color.black, "hole-small"));
        program.setDisplayInformation(AdultWolf.class, new DisplayInformation(Color.gray, "wolf"));
        program.setDisplayInformation(AdultBear.class, new DisplayInformation(Color.yellow, "bear"));
        program.setDisplayInformation(Carcass.class, new DisplayInformation(Color.magenta, "carcass"));
        program.setDisplayInformation(Mushroom.class, new DisplayInformation(Color.pink, "fungi"));
        return program;
    }

    /**
     * Builds the world, and fills it with the given content. If there is no given content, the world will just return empty.
     * If there is not enough room for everything, as many as possible are placed.
     * @param world The world to fill.
     * @param worldInfo The content to build, as read by FileIO.
     * @return The world object filled with content.
     */
    public World buildWorld(World world, WorldInfo worldInfo){
        for (SpawnInfo spawn : worldInfo.spawns()) {
            String type = spawn.type();
            if(!isKnownType(type)){
                System.out.println("No specification made for type: "+type);
                continue;
            }

            int amountToInstansiate = random.nextInt(spawn.min(), spawn.max() + 1);
            List<Location> freeLocations = findFreeLocations(world, type);

            // The first one goes to the given (x,y), if the file has one and it is free.
            if(spawn.hasLocation() && amountToInstansiate > 0 && freeLocations.remove(spawn.location())){
                place(world, type, spawn.location());
                amountToInstansiate--;
            }

            Collections.shuffle(freeLocations, random);
            if(freeLocations.size() < amountToInstansiate){
                System.out.println("Not enough room for " + amountToInstansiate + " " + type
                    + ", placing " + freeLocations.size() + " instead.");
                amountToInstansiate = freeLocations.size();
            }

            for (int i = 0; i < amountToInstansiate; i++) {
                place(world, type, freeLocations.get(i));
            }
        }

        return world;
    }

    /**
     * Finds every tile where an object of the given type could be placed.
     * Blocking objects need a tile without another blocking object.
     * Non-blocking objects need a tile without another non-blocking object.
     */
    private List<Location> findFreeLocations(World world, String type){
        List<Location> freeLocations = new ArrayList<>();
        for (int x = 0; x < world.getSize(); x++) {
            for (int y = 0; y < world.getSize(); y++) {
                Location location = new Location(x, y);
                boolean free;
                if(type.equals("rabbit")){
                    free = world.isTileEmpty(location);
                }else{
                    free = !world.containsNonBlocking(location);
                }
                if(free){
                    freeLocations.add(location);
                }
            }
        }
        return freeLocations;
    }

    private void place(World world, String type, Location location){
        switch (type) {
            case "rabbit":
                world.setTile(location, new AdultRabbit());
                break;
            case "grass":
                world.setTile(location, new Grass());
                break;
            case "burrow":
                new Rabbithole().makeEntrance(location, world);
                break;
            case "wolf":
                world.setTile(location, new AdultWolf(new WolfPack()));
                break;
            case "bear":
                Bear bear = new AdultBear();
                Territory territory = new Territory(location, bear, 2, world);
                bear.setTerritory(territory);
                world.setTile(location, bear);
                break;
        }
    }

    private boolean isKnownType(String type){
        return type.equals("rabbit") || type.equals("grass") || type.equals("burrow")||type.equals("wolf")|| type.equals("bear");
        
    }

}
