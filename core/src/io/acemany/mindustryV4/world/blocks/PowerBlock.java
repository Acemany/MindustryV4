package io.acemany.mindustryV4.world.blocks;

import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.meta.BlockGroup;

public abstract class PowerBlock extends Block{

    public PowerBlock(String name){
        super(name);
        update = true;
        solid = true;
        hasPower = true;
        group = BlockGroup.power;
    }
}
