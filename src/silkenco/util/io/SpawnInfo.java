package silkenco.util.io;

import itumulator.world.Location;

/**
 * One line of a world file, e.g. "grass 10-15" or "bear 1 (3,5)".
 * @param type     what to spawn, e.g. "rabbit".
 * @param min      the lowest amount to spawn.
 * @param max      the highest amount to spawn (inclusive). Same as min when the file gives no range.
 * @param location where to spawn, or null when the file gives no (x,y).
 */
public record SpawnInfo(String type, int min, int max, Location location) {

    public boolean hasLocation() {
        return location != null;
    }
}
