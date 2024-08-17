package io.acemany.mindustryV4.maps.missions;

import io.acemany.mindustryV4.game.GameMode;
import io.anuke.ucore.scene.ui.layout.Table;

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
    public GameMode getMode(){
        return GameMode.victory;
    }

    @Override
    public void display(Table table){

    }
}
