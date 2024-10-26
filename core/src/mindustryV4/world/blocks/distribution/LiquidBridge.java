package mindustryV4.world.blocks.distribution;

import mindustryV4.type.Item;
import mindustryV4.type.Liquid;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockGroup;
import ucore.core.Timers;
import ucore.util.Mathf;

import static mindustryV4.Vars.world;

public class LiquidBridge extends ItemBridge{

    public LiquidBridge(String name){
        super(name);
        hasItems = false;
        hasLiquids = true;
        outputsLiquid = true;
        group = BlockGroup.liquids;
    }

    @Override
    public void update(Tile tile){
        ItemBridgeEntity entity = tile.entity();

        entity.time += entity.cycleSpeed * Timers.delta();
        entity.time2 += (entity.cycleSpeed - 1f) * Timers.delta();

        Tile other = world.tile(entity.link);
        if(!linkValid(tile, other) ){
            tryDumpLiquid(tile, entity.liquids.current());
        }else{
            if(entity.cons.valid()){
                entity.uptime = Mathf.lerpDelta(entity.uptime, 1f, 0.04f);
            }else{
                entity.uptime = Mathf.lerpDelta(entity.uptime, 0f, 0.02f);
            }

            if(entity.uptime >= 0.5f){

                if(tryMoveLiquid(tile, other, false, entity.liquids.current()) > 0.1f){
                    entity.cycleSpeed = Mathf.lerpDelta(entity.cycleSpeed, 4f, 0.05f);
                }else{
                    entity.cycleSpeed = Mathf.lerpDelta(entity.cycleSpeed, 1f, 0.01f);
                }
            }
        }
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        return false;
    }

    @Override
    public boolean canDumpLiquid(Tile tile, Tile to, Liquid liquid){
        ItemBridgeEntity entity = tile.entity();

        Tile other = world.tile(entity.link);
        if(!linkValid(tile, other)){
            return !(to.block() instanceof LiquidBridge);
        }

        int rel = tile.absoluteRelativeTo(other.x, other.y);
        int rel2 = tile.relativeTo(to.x, to.y);

        return rel != rel2;
    }
}
