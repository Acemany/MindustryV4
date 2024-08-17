package io.acemany.mindustryV4.world.blocks.power;

import io.acemany.mindustryV4.type.Liquid;
import io.acemany.mindustryV4.world.Tile;
import io.acemany.mindustryV4.world.consumers.ConsumeLiquid;

public class TurbineGenerator extends BurnerGenerator{

    public TurbineGenerator(String name){
        super(name);
        singleLiquid = false;

        consumes.require(ConsumeLiquid.class);
    }

    @Override
    public boolean acceptLiquid(Tile tile, Tile source, Liquid liquid, float amount){
        return super.acceptLiquid(tile, source, liquid, amount) || liquid == consumes.liquid() && tile.entity.liquids.get(consumes.liquid()) < liquidCapacity;
    }
}
