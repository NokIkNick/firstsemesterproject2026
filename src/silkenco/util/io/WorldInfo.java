package silkenco.util.io;

import java.util.List;

/**
 * Everything read from a world file.
 * @param size   the size of the world.
 * @param spawns one entry per line after the size, in file order.
 */
public record WorldInfo(int size, List<SpawnInfo> spawns) {
}
