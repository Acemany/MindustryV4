package io.acemany.mindustryV4.world.blocks.defense;

import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.meta.BlockGroup;

public class Wall extends Block{

    public Wall(String name){
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.walls;
    }

    @Override
    public boolean canReplace(Block other){
        return super.canReplace(other) && health > other.health;
    }

}
