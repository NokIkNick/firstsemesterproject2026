package silkenco.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.World;
import silkenco.actors.AdultRabbit;

public class WorldBuilderTest {
    WorldBuilder worldBuilder;
    World world;

    @BeforeEach
    public void setup() {
        worldBuilder = new WorldBuilder();
        world = new World(5);
    }

    @Test
    public void emptyContentReturnsEmptyWorld() {
        World result = worldBuilder.buildWorld(world, new HashMap<>(), 5);

        assertSame(world, result);
        assertTrue(result.getEntities().isEmpty());
    }

    @Test
    public void buildWorldPlacesRabbits() {
        Map<String, String> content = new HashMap<>();
        content.put("size", "5");
        content.put("rabbit", "3");

        World result = worldBuilder.buildWorld(world, content, 5);

        assertEquals(3, result.getEntities().size());
        for (Object entity : result.getEntities().keySet()) {
            assertTrue(entity instanceof AdultRabbit);
        }
    }
}
