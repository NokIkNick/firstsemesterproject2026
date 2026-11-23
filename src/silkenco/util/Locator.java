package silkenco.util;

import java.util.Collection;
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
        surroundingTiles.add(origin);
        Set<T> foundTargets = world.getAll(target, surroundingTiles);
        return locateClosestFromCollection(world, origin, foundTargets);
    }

    /**
     * Finds the closest object of the given type to origin, from a collection.
     * Returns null if none is found.
     */
    public static <T> T locateClosestFromCollection(World world, Location origin, Collection<T> targets){
        T closest = null;
        double lowestDist = Double.MAX_VALUE;
        for (T object : targets){
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

    /**
     * 
     * @param number
     * @return either a 1 or a negative 1 depending on which ones the nubmer is closer to.
     */
    public static int toNearestOne(int number){
        if (number<0){
            return -1;
        }
        else if(number>0){
            return 1;
        }
        else{
            return 0;
        }
    }

}
