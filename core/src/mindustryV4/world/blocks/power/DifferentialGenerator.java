package mindustryV4.world.blocks.power;

import mindustryV4.type.Item;
import mindustryV4.type.Liquid;
import mindustryV4.world.Tile;
import mindustryV4.world.consumers.ConsumeItem;
import mindustryV4.world.consumers.ConsumeLiquid;

public class DifferentialGenerator extends TurbineGenerator{

    public DifferentialGenerator(String name){
        super(name);

        hasLiquids = true;
        consumes.require(ConsumeItem.class);
        consumes.require(ConsumeLiquid.class);
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        return hasItems && consumes.item() == item && tile.entity.items.total() < itemCapacity;
    }

    @Override
    public boolean acceptLiquid(Tile tile, Tile source, Liquid liquid, float amount){
        return hasLiquids && consumes.liquid() == liquid && tile.entity.liquids.get(liquid) < liquidCapacity;
    }
}
