package silkenco.actors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;
import silkenco.actors.AdultRabbit;
import silkenco.actors.Rabbit;

public class RabbitTest {
    World world;


    @BeforeEach 
    public void setup(){
        world = new World(5);
    }

    @Test
    public void rabbitMoves(){
        Rabbit rabbit = new AdultRabbit();
        Location currentLocation = new Location(0, 0);

        world.setTile(currentLocation, rabbit);
        world.setCurrentLocation(currentLocation);

        //Simuler movement.
        rabbit.act(world);

        assertNull(world.getTile(currentLocation));
        assertNotNull(currentLocation);
        assertNotEquals(world.getLocation(rabbit), currentLocation);
    }

    @Test 
    public void rabbitDiesOfOldAge(){
        Rabbit rabbit = new AdultRabbit();
        world.setTile(new Location(0, 0), rabbit);

        // Act until just before max age
        while (rabbit.getAge() < rabbit.getMaxAge()) {
            world.setCurrentLocation(world.getLocation(rabbit));
            rabbit.act(world);
        }
        //Rabbit er stadigvæk i live.
        assertTrue(world.contains(rabbit));

        world.setCurrentLocation(world.getLocation(rabbit));
        rabbit.act(world);

        //Rabbit er død.
        assertFalse(world.contains(rabbit));
    }

    @Test 
    public void rabbitdiesOfHunger(){
        Rabbit rabbit = new AdultRabbit();
        world.setTile(new Location(0, 0), rabbit);

        world.setCurrentLocation(world.getLocation(rabbit));
        rabbit.act(world);

        //Rabbit er i live.
        assertTrue(world.contains(rabbit));

        rabbit.setHunger(-1);

        world.setCurrentLocation(world.getLocation(rabbit));
        rabbit.act(world);

        //Rabbit burde være død.
        assertFalse(world.contains(rabbit));


    }
}
