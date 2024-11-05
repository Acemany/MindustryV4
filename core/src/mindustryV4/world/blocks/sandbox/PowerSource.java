package mindustryV4.world.blocks.sandbox;

import mindustryV4.world.Tile;
import mindustryV4.world.blocks.power.PowerNode;

public class PowerSource extends PowerNode{

    public PowerSource(String name){
        super(name);
        maxNodes = 100;
        outputsPower = true;
        consumesPower = false;
    }

    @Override
    public float getPowerProduction(Tile tile){
        return 10000f;
    }

}
