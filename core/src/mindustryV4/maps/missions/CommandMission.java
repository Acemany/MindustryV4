package mindustryV4.maps.missions;

import mindustryV4.Vars;
import mindustryV4.entities.units.BaseUnit;
import mindustryV4.entities.units.UnitCommand;
import ucore.util.Bundles;

public class CommandMission extends Mission{
    private final UnitCommand command;

    public CommandMission(UnitCommand command){
        this.command = command;
    }

    @Override
    public boolean isComplete(){
        for(BaseUnit unit : Vars.unitGroups[Vars.defaultTeam.ordinal()].all()){
            if(unit.isCommanded() && unit.getCommand() == command){
                return true;
            }
        }
        return false;
    }

    @Override
    public String displayString(){
        return Bundles.format("text.mission.command", command.localized());
    }
}
