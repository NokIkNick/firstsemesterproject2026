package silkenco.actors;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;
import silkenco.nonblocking.Entrancehole;

public class AdultRabbitTest {
    World world;

    @BeforeEach 
    public void setup(){
        world = new World(2);
    }

    @Test
    public void adultRabbitDigsAHole(){
        AdultRabbit rabbit = new AdultRabbit();
        world.setTile(new Location(0, 0), rabbit);
        world.setCurrentLocation(world.getLocation(rabbit));
        rabbit.act(world);
        
        Object entrance = world.getNonBlocking(world.getLocation(rabbit));
        assertNotNull(entrance);
        assertInstanceOf(Entrancehole.class, entrance);
    } 

    @Test 
    public void adultRabbitReproduces(){
        AdultRabbit rabbit = new AdultRabbit();
        AdultRabbit rabbit2 = new AdultRabbit();

        world.setTile(new Location(0, 0), rabbit);
        world.setTile(new Location(1, 0), rabbit2);

        world.setCurrentLocation(world.getLocation(rabbit));
        for (int i = 0; i < 10; i++) {
            rabbit.act(world);
            rabbit2.act(world);
            world.setCurrentLocation(world.getLocation(rabbit));
        }

        Set<Location> surroundingTiles = world.getSurroundingTiles(world.getCurrentLocation(), 4);
        Set<BabyRabbit> surroundingRabbits = world.getAll(BabyRabbit.class, surroundingTiles);
        Object baby = surroundingRabbits.toArray()[0];

        assertNotNull(baby);
        assertInstanceOf(BabyRabbit.class, baby);

    }

}
