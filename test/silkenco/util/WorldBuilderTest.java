package silkenco.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;
import silkenco.actors.AdultRabbit;
import silkenco.nonblocking.Entrancehole;
import silkenco.util.io.SpawnInfo;
import silkenco.util.io.WorldInfo;

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
        World result = worldBuilder.buildWorld(world, new WorldInfo(5, new ArrayList<>()));

        assertSame(world, result);
        assertTrue(result.getEntities().isEmpty());
    }

    @Test
    public void buildWorldPlacesRabbits() {
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("rabbit", 3, 3, null)));

        World result = worldBuilder.buildWorld(world, info);

        assertEquals(3, result.getEntities().size());
        for (Object entity : result.getEntities().keySet()) {
            assertTrue(entity instanceof AdultRabbit);
        }
    }

    @Test
    public void duplicateLinesAreBothSpawned() {
        WorldInfo info = new WorldInfo(5, List.of(
            new SpawnInfo("rabbit", 2, 2, null),
            new SpawnInfo("rabbit", 2, 2, null)));

        World result = worldBuilder.buildWorld(world, info);

        assertEquals(4, result.getEntities().size());
    }

    @Test
    public void rangeMaxIsInclusive() {
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("rabbit", 4, 4, null)));

        assertEquals(4, worldBuilder.buildWorld(world, info).getEntities().size());
    }

    @Test
    public void spawnUsesGivenLocation() {
        Location location = new Location(3, 4);
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("rabbit", 1, 1, location)));

        World result = worldBuilder.buildWorld(world, info);
        Object rabbit = result.getEntities().keySet().iterator().next();

        assertEquals(location, result.getLocation(rabbit));
    }

    @Test
    public void tooManyToFitDoesNotLoopForever() {
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("rabbit", 30, 30, null)));

        World result = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> worldBuilder.buildWorld(world, info));

        assertEquals(25, result.getEntities().size());
    }

    @Test
    public void burrowsArePlaced() {
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("burrow", 3, 3, null)));

        World result = worldBuilder.buildWorld(world, info);

        assertEquals(3, result.getAll(Entrancehole.class, allTiles()).size());
    }

    @Test
    public void unknownTypeIsSkipped() {
        WorldInfo info = new WorldInfo(5, List.of(new SpawnInfo("wolf", 2, 2, null)));

        assertTrue(worldBuilder.buildWorld(world, info).getEntities().isEmpty());
    }

    private Set<Location> allTiles() {
        Set<Location> tiles = new HashSet<>();
        for (int x = 0; x < world.getSize(); x++) {
            for (int y = 0; y < world.getSize(); y++) {
                tiles.add(new Location(x, y));
            }
        }
        return tiles;
    }
}
