package silkenco.actors;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;
import silkenco.util.Locator;
import silkenco.enums.AnimalState;

public abstract class Animal implements Actor{
    
    protected int hunger;
    protected int age;
    protected int energy;
    protected int currentEnergy;
    protected int maxAge;
    protected int health;
    protected AnimalState currentState;
    

    public Animal(int hunger, int energy, int maxAge, int age, int health){
        this.hunger = hunger;
        this.age = age;
        this.energy = energy;
        this.currentEnergy = this.energy;
        this.maxAge = maxAge;
        this.health = health; 
        currentState = AnimalState.ROAMING;
    }

    public void moveTowards(Location location, World world){ 
        int LocationX = location.getX();
        int LocationY = location.getY();
        int thisLocationX = world.getLocation(this).getX();
        int thisLocationY = world.getLocation(this).getY();
        Location newloc;
        if (!(LocationX - thisLocationX == 0) && !(LocationY - thisLocationY == 0)){
            //move diagonally
            newloc = new Location(thisLocationX + Locator.toNearestOne(LocationX - thisLocationX),thisLocationY + Locator.toNearestOne(LocationY - thisLocationY));
            
        }
        else if(!(LocationX - thisLocationX == 0)){
                //move towards X
                newloc = new Location(thisLocationX + Locator.toNearestOne(LocationX - thisLocationX),thisLocationY);
                

        }
        else if(!(LocationY - thisLocationY == 0)){
                //Move towards Y
                newloc = new Location(thisLocationX,thisLocationY + Locator.toNearestOne(LocationY - thisLocationY));
                
        }
        else{
            //Already at target!!!
            newloc = world.getLocation(this);
        }
        if (world.isTileEmpty(newloc)){
            world.move(this, newloc);
        }
        
    }

    protected void sleep(){
        currentEnergy = energy;
    }

    public void act(World world){
        if(this.hunger <= 0 || this.age > this.maxAge || this.health <= 0){
            this.die(world);
            return;
        }

        this.age++;
    }

    public void useEnergy(){
        if (currentEnergy <= 0) {
            throw new RuntimeException("Could not reduce energy, due to energy being too low."); //TODO make proper exception
        }
        currentEnergy = currentEnergy - 10;

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHunger() {
        return hunger;
    }

    protected abstract void move(World world);
    protected abstract void die(World world);
    
}
