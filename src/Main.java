import itumulator.executable.Program;
import silkenco.util.WorldBuilder;
import silkenco.util.io.FileIO;
import silkenco.util.io.WorldInfo;


public class Main {

    static WorldBuilder worldBuilder = new WorldBuilder();
    static FileIO fileIO = new FileIO();

    public static void main(String[] args) {
        WorldInfo worldInfo = fileIO.read("t1-2cde.txt");
        Program program = worldBuilder.buildProgram(worldInfo.size(), 800, 1000);

        worldBuilder.buildWorld(program.getWorld(), worldInfo);

        program.show();
        while(true){
            program.simulate();
        }
    }



}