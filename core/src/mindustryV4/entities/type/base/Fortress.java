package mindustryV4.entities.type.base;

import mindustryV4.entities.Units;
import mindustryV4.entities.type.GroundUnit;

public class Fortress extends GroundUnit{

    @Override
    protected void patrol(){
        if(Units.invalidateTarget(target, this)){
            super.patrol();
        }
    }

    @Override
    protected void moveToCore(){
        if(Units.invalidateTarget(target, this)){
            super.moveToCore();
        }
    }
}
