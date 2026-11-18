import itumulator.executable.Program;
import itumulator.world.World;
import java.util.Map;
import util.io.FileIO;
import util.io.WorldBuilder;

public class Main {

    static WorldBuilder worldBuilder = new WorldBuilder();
    static FileIO fileIO = new FileIO();

    public static void main(String[] args) {
        Map<String,String> worldInfoMap = fileIO.read("t1-2a.txt");
        int size = Integer.parseInt(worldInfoMap.get("size"));
        Program program = worldBuilder.buildProgram(size, 800, 1000);
        
        World w = worldBuilder.buildWorld(program.getWorld(), worldInfoMap, size);

        program.show();
        while(true){
            program.simulate();
        }
    }



}