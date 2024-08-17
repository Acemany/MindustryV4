package io.acemany.mindustryV4.world.blocks.power;

import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.type.Liquid;

public class BurnerGenerator extends ItemLiquidGenerator{

    public BurnerGenerator(String name){
        super(name);
    }

    @Override
    protected float getLiquidEfficiency(Liquid liquid){
        return liquid.flammability;
    }

    @Override
    protected float getItemEfficiency(Item item){
        return item.flammability;
    }
}
