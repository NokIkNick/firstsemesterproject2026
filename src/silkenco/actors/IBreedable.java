package silkenco.actors;

import itumulator.world.World;

/**
 * IBreedable
 */
public interface IBreedable<T extends Animal> {

    T detectMate(World world);
    void reproduce(World world, T chosenMate);
}