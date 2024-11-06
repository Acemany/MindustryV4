package mindustryV4.world.blocks.storage;

import mindustryV4.entities.Effects;
import mindustryV4.Vars;
import mindustryV4.content.Fx;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.type.Item;
import mindustryV4.type.ItemType;
import mindustryV4.world.Tile;

import static mindustryV4.Vars.data;
import static mindustryV4.Vars.world;

public class LaunchPad extends StorageBlock{
    protected final int timerLaunch = timers++;
    /**Time inbetween launches.*/
    protected float launchTime;

    public LaunchPad(String name){
        super(name);
        update = true;
        hasItems = true;
        solid = true;
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        return item.type == ItemType.material && super.acceptItem(item, tile, source);
    }

    @Override
    public void update(Tile tile){
        TileEntity entity = tile.entity;

        if(entity.cons.valid() && !world.getMap().custom){
            for(Item item : Vars.content.items()){
                if(entity.items.get(item) >= itemCapacity && entity.timer.get(timerLaunch, launchTime)){
                    //TODO play animation of some sort
                    Effects.effect(Fx.dooropenlarge, tile);
                    data.addItem(item, entity.items.get(item));
                    entity.items.set(item, 0);
                }
            }
        }
    }
}
