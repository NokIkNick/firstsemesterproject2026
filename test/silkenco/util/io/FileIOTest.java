package silkenco.util.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class FileIOTest {

    @Test
    public void readReturnsSizeAndObjects() {
        FileIO fileIO = new FileIO();

        // data/t1-1a.txt contains "5" followed by "grass 3"
        Map<String, String> content = fileIO.read("t1-1a.txt");

        assertEquals("5", content.get("size"));
        assertEquals("3", content.get("grass"));
        assertEquals(2, content.size());
    }

    @Test
    public void readMissingFileReturnsEmptyMap() {
        FileIO fileIO = new FileIO();

        Map<String, String> content = fileIO.read("does-not-exist.txt");

        assertTrue(content.isEmpty());
    }
}
