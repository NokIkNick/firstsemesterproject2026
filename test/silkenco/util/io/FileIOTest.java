package silkenco.util.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UncheckedIOException;

import org.junit.jupiter.api.Test;

import itumulator.world.Location;

public class FileIOTest {

    FileIO fileIO = new FileIO();

    @Test
    public void readReturnsSizeAndObjects() {
        WorldInfo info = fileIO.read("t1-1a.txt");

        assertEquals(5, info.size());
        assertEquals(1, info.spawns().size());

        SpawnInfo grass = info.spawns().get(0);
        assertEquals("grass", grass.type());
        assertEquals(3, grass.min());
        assertEquals(3, grass.max());
        assertFalse(grass.hasLocation());
    }

    @Test
    public void readParsesRange() {
        SpawnInfo grass = fileIO.read("t1-1c.txt").spawns().get(0);

        assertEquals(20, grass.min());
        assertEquals(30, grass.max());
    }

    @Test
    public void readKeepsDuplicateTypes() {
        WorldInfo info = fileIO.read("tf2-1.txt");

        assertEquals(4, info.spawns().size());
        assertEquals("wolf", info.spawns().get(0).type());
        assertEquals("wolf", info.spawns().get(1).type());
    }

    @Test
    public void readParsesLocation() {
        SpawnInfo bear = fileIO.read("t2-4a.txt").spawns().get(0);

        assertTrue(bear.hasLocation());
        assertEquals(new Location(3, 5), bear.location());
    }

    @Test
    public void readWithoutLocationGivesNull() {
        SpawnInfo rabbit = fileIO.read("t1-2a.txt").spawns().get(0);

        assertNull(rabbit.location());
    }

    @Test
    public void readMissingFileThrows() {
        assertThrows(UncheckedIOException.class, () -> fileIO.read("does-not-exist.txt"));
    }
}
