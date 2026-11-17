import java.awt.Color;
import java.util.Map;

import actors.AdultRabbit;
import actors.BabyRabbit;
import actors.Rabbit;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import util.io.FileIO;
import util.io.WorldBuilder;

public class Main {

    static WorldBuilder worldBuilder = new WorldBuilder();
    static FileIO fileIO = new FileIO();

    public static void main(String[] args) {
        Map<String,String> worldInfoMap = fileIO.read("t1-2b.txt");
        int size = Integer.parseInt(worldInfoMap.get("size"));
        Program program = worldBuilder.buildProgram(size, 800, 3000);
        
        World w = worldBuilder.buildWorld(program.getWorld(), worldInfoMap, size);

        program.show();
        while(true){
            program.simulate();
        }
    }



}