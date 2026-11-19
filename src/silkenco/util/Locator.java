package silkenco.util;

import java.util.Set;

import itumulator.world.Location;
import itumulator.world.World;

public class Locator {

    /**
     * Finds the closest object of the given type to origin.
     * Returns null if none is found.
     */
    public static <T> T locateClosest(World world, Location origin, Class<T> target){
        Set<Location> surroundingTiles = world.getSurroundingTiles(origin, world.getSize());
        Set<T> foundTargets = world.getAll(target, surroundingTiles);
        T closest = null;
        double lowestDist = Double.MAX_VALUE;
        for (T object : foundTargets){
            int xdiff = origin.getX() - world.getLocation(object).getX();
            int ydiff = origin.getY() - world.getLocation(object).getY();
            double distance = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
            if (distance < lowestDist){
                lowestDist = distance;
                closest = object;
            }
        }
        return closest;
    }

}
