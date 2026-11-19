import java.util.Map;

import itumulator.executable.Program;
import itumulator.world.World;
import silkenco.util.WorldBuilder;
import silkenco.util.io.FileIO;


public class Main {

    static WorldBuilder worldBuilder = new WorldBuilder();
    static FileIO fileIO = new FileIO();

    public static void main(String[] args) {
        Map<String,String> worldInfoMap = fileIO.read("t1-1c.txt");
        int size = Integer.parseInt(worldInfoMap.get("size"));
        Program program = worldBuilder.buildProgram(size, 800, 1000);
        
        World w = worldBuilder.buildWorld(program.getWorld(), worldInfoMap, size);

        program.show();
        while(true){
            program.simulate();
        }
    }



}