package mindustryV4.world.blocks;

import mindustryV4.world.Block;
import mindustryV4.world.meta.BlockGroup;

public abstract class PowerBlock extends Block{

    public PowerBlock(String name){
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        group = BlockGroup.power;
    }
}
