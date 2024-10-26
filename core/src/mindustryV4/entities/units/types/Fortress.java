package mindustryV4.entities.units.types;

import mindustryV4.entities.Units;
import mindustryV4.entities.units.GroundUnit;

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
