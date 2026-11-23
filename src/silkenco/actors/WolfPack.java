package silkenco.actors;

import java.util.*;

public class WolfPack {
    private List<Wolf> members = new ArrayList<>();
    private Rabbit target; 

    public void addWolf(Wolf wolf){
        members.add(wolf);
    }

    public List<Wolf> getMembers(){
        return members;
    }

    public Rabbit getTarget(){
        return target; 
    }

    public void chooseTarget(Rabbit target){
        this.target = target; 
    }
}
