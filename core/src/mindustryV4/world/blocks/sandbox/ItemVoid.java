package mindustryV4.world.blocks.sandbox;

import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;

public class ItemVoid extends Block{

    public ItemVoid(String name){
        super(name);
        update = solid = true;
    }

    @Override
    public void handleItem(Item item, Tile tile, Tile source){}

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        return true;
    }
}
