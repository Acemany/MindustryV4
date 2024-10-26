package mindustryV4.world.blocks.distribution;

import mindustryV4.world.Tile;
import mindustryV4.world.blocks.LiquidBlock;

public class LiquidRouter extends LiquidBlock{

    public LiquidRouter(String name){
        super(name);
    }

    @Override
    public void update(Tile tile){

        if(tile.entity.liquids.total() > 0.01f){
            tryDumpLiquid(tile, tile.entity.liquids.current());
        }
    }

}
