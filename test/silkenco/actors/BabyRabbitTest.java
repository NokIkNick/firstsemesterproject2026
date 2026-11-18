package silkenco.actors;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

public class BabyRabbitTest {
    World world;

    @BeforeEach 
    public void setup(){
        world = new World(4);
    }

    @Test 
    public void babyRabbitGrowsUp(){
        BabyRabbit babyRabbit = new BabyRabbit();
        world.setTile(new Location(0, 0), babyRabbit);
        world.setCurrentLocation(world.getLocation(babyRabbit));

        babyRabbit.act(world);

        assertInstanceOf(BabyRabbit.class, babyRabbit);

        Location prevLocation = new Location(0, 0);
        for (int i = 0; i < 9; i++) {
            babyRabbit.act(world);
            prevLocation = world.getLocation(babyRabbit);
            world.setCurrentLocation(prevLocation);
        }

        Object rabbit = world.getTile(prevLocation);
        assertInstanceOf(BabyRabbit.class, rabbit);

        //Baby vokser up.
        babyRabbit.act(world);

        Set<Location> surroundingTiles = world.getSurroundingTiles(new Location(0,0), 4);
        Set<AdultRabbit> surroundingRabbits = world.getAll(AdultRabbit.class, surroundingTiles);
        Object adult = surroundingRabbits.toArray()[0];

        assertNotNull(adult);
        assertInstanceOf(AdultRabbit.class, adult);
    }
}
