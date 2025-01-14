package mindustryV4.maps.missions;

import mindustryV4.Vars;
import mindustryV4.entities.units.BaseUnit;
import mindustryV4.entities.units.UnitType;
import ucore.util.Bundles;

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
        return Bundles.format("mission.unit", type.localizedName());
    }
}
