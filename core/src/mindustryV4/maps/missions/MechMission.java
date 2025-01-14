package mindustryV4.maps.missions;

import mindustryV4.Vars;
import mindustryV4.entities.Player;
import mindustryV4.type.Mech;
import ucore.util.Bundles;

public class MechMission extends Mission{
    private final Mech mech;

    public MechMission(Mech mech){
        this.mech = mech;
    }

    @Override
    public boolean isComplete(){
        for(Player player : Vars.playerGroup.all()){
            if(player.mech == mech){
                return true;
            }
        }
        return false;
    }

    @Override
    public String displayString(){
        return Bundles.format("mission.mech", mech.localizedName());
    }
}
