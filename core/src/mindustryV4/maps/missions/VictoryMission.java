package mindustryV4.maps.missions;

import io.anuke.arc.scene.ui.layout.Table;

public class VictoryMission extends Mission{
    @Override
    public boolean isComplete(){
        return false;
    }

    @Override
    public String displayString(){
        return "none";
    }

    @Override
    public void display(Table table){

    }
}
