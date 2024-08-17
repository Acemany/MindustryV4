package io.acemany.mindustryV4.maps.missions;

import io.acemany.mindustryV4.Vars;
import io.acemany.mindustryV4.entities.Player;
import io.acemany.mindustryV4.type.Mech;
import io.anuke.ucore.util.Bundles;

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
        return Bundles.format("text.mission.mech", mech.localizedName());
    }
}
