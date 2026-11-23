package silkenco.actors;

import java.util.*;
import silkenco.actors.*;

public class WolfPack {
    private List<Wolf> members = new ArrayList<>();
    private Rabbit target; 
    private AdultWolf alpha;

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

    public Wolf getAlpha(){
        return alpha;
    }
}
