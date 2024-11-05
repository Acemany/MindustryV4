package mindustryV4.world.blocks.sandbox;

import mindustryV4.type.Item;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.distribution.Sorter;

public class ItemSource extends Sorter{

    public ItemSource(String name){
        super(name);
        hasItems = true;
        update = true;
        solid = true;
    }

    @Override
    public boolean outputsItems(){
        return true;
    }

    @Override
    public void update(Tile tile){
        SorterEntity entity = tile.entity();
        if(entity.sortItem == null) return;

        entity.items.set(entity.sortItem, 1);
        tryDump(tile, entity.sortItem);
        entity.items.set(entity.sortItem, 0);
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        return false;
    }
}
