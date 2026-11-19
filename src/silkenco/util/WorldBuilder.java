package silkenco.util;

import java.awt.Color;
import java.util.Map;
import java.util.Random;
import silkenco.actors.Grass;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import silkenco.actors.AdultRabbit;
import silkenco.actors.BabyRabbit;
import silkenco.nonblocking.Entrancehole;
import silkenco.nonblocking.Rabbithole;

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
        program.setDisplayInformation(BabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small"));
        program.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));
        program.setDisplayInformation(Entrancehole.class, new DisplayInformation(Color.black, "hole"));
        return program;
    }

    /**
     * Builds the world, and fills it with the given content. If there is no given content, the world will just return empty.
     * @param world The world to fill.
     * @param worldContent The content to build 
     * @param worldSize Size of the world, the same parameter for the program.
     * @return The world object filled with content.
     */
    public World buildWorld(World world, Map<String, String> worldContent, int worldSize){
        if(worldContent.size() <= 0) return world;

        System.out.println("building world");
        for (String type : worldContent.keySet()) {
            if(type.equals("size")) continue;

            int instansiated = 0;
            int amountToInstansiate;

            if(worldContent.get(type).contains("-")){
                int low = Integer.parseInt(worldContent.get(type).split("-")[0]);
                int high = Integer.parseInt(worldContent.get(type).split("-")[1]);
                amountToInstansiate = random.nextInt(low, high);
            }else {
                amountToInstansiate = Integer.parseInt(worldContent.get(type));
            }

            while (instansiated < amountToInstansiate) {
                System.out.println("Instansiating... "+instansiated);
                int x = random.nextInt(worldSize);
                int y = random.nextInt(worldSize);
                Location location = new Location(x, y);

                if(!world.isTileEmpty(location)) continue;

                switch (type) {
                    case "rabbit":
                        world.setTile(location, new AdultRabbit());
                        break;
                    case "grass":
                        if(world.containsNonBlocking(location)) continue;
                        world.setTile(location, new Grass());
                        break;
                    case "burrow":
                        if(world.containsNonBlocking(location)) continue;
                        world.setTile(location, new Entrancehole(location, new Rabbithole()));
                        break;
                    default:
                        System.out.println("No specification made for type: "+type);
                        break;
                }
                instansiated++;
                
            }
        }

        return world;
    }

}
