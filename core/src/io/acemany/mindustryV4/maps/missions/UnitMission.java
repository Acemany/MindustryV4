package io.acemany.mindustryV4.maps.missions;

import io.acemany.mindustryV4.Vars;
import io.acemany.mindustryV4.entities.units.BaseUnit;
import io.acemany.mindustryV4.entities.units.UnitType;
import io.anuke.ucore.util.Bundles;

public class UnitMission extends Mission{
    private final UnitType type;

    public UnitMission(UnitType type){
        this.type = type;
    }

    @Override
    public boolean isComplete(){
        for(BaseUnit unit : Vars.unitGroups[Vars.defaultTeam.ordinal()].all()){
            if(unit.getType() == type){
                return true;
            }
        }
        return false;
    }

    @Override
    public String displayString(){
        return Bundles.format("text.mission.unit", type.localizedName());
    }
}
