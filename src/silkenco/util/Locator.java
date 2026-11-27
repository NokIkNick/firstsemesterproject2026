package silkenco.util;

import itumulator.world.Location;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Locator {
    /**
     * 
     * @param <T>
     * @param world
     * @param target any class
     * @return A list of type <Object> containing all instances of target class, if no instances are present, returns null
     */
    public static <T> List<T> LocateAll(World world, Class<T> target){
        Set<Location> AllTiles = world.getSurroundingTiles(new Location(0, 0), world.getSize());
        AllTiles.add(new Location(0, 0));
        List<T> objects = new ArrayList<>();

        for(Location l: AllTiles){
            //since target does not have a value at runtime, instanceof is impossible to use like normal
            if (target.isInstance(world.getTile(l))){
                //Unsafe downcasting
                objects.add((T) world.getTile(l));
            }
        }
        if (objects.isEmpty()){
            return null;
        }
        return objects;

    }

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
            double distance = distanceTo(world,origin,world.getLocation(object));
            if (distance < lowestDist){
                lowestDist = distance;
                closest = object;
            }
        }
        return closest;
    }
    /**
     * Finds the distance between to locations
     * @param world
     * @param origin Location 1
     * @param target Location 2
     * @return The distance between location 1 and location 2 in the form of a double
     */
    public static double distanceTo(World world, Location origin, Location target){
        int xdiff = origin.getX() - target.getX();
        int ydiff = origin.getY() - target.getY();
        double distance = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
        return distance;
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
