package mindustryV4.world.blocks.power;

import mindustryV4.world.Tile;
import mindustryV4.world.blocks.PowerBlock;

public class PowerDistributor extends PowerBlock{

    public PowerDistributor(String name){
        super(name);
        consumesPower = false;
        outputsPower = true;
    }

    @Override
    public void update(Tile tile){
        tile.entity.power.graph.update();
    }
}
