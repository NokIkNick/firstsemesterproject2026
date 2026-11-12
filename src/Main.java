import java.awt.Color;

import actors.AdultRabbit;
import actors.BabyRabbit;
import actors.Rabbit;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 5;
        Program p = new Program(size, 800, 3000);
        p.setDisplayInformation(AdultRabbit.class, new DisplayInformation(Color.darkGray, "rabbit-large"));
        p.setDisplayInformation(BabyRabbit.class, new DisplayInformation(Color.gray, "rabbit-small"));

        World w = p.getWorld();

        Rabbit adultRabbit = new AdultRabbit();
        Rabbit adulRabbit2 = new AdultRabbit();
        
        
        w.setTile(new Location(0, 1), adultRabbit);
        w.setTile(new Location(0, 2), adulRabbit2);
        
        p.show();
        while(true){
            p.simulate();
        }
    }
}