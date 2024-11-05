package mindustryV4.world.consumers;

import io.anuke.arc.collection.Array;
import io.anuke.arc.function.Predicate;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.type.Item;
import mindustryV4.type.Item.Icon;
import mindustryV4.ui.ItemImage;
import mindustryV4.ui.MultiReqImage;
import mindustryV4.ui.ReqImage;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.values.ItemFilterValue;

import static mindustryV4.Vars.content;

public class ConsumeItemFilter extends Consume{
    private final Predicate<Item> filter;

    public ConsumeItemFilter(Predicate<Item> item){
        this.filter = item;
    }

    @Override
    public void build(Tile tile, Table table){
        Array<Item> list = content.items().select(filter);
        MultiReqImage image = new MultiReqImage();
        list.each(item -> image.add(new ReqImage(new ItemImage(item.icon(Icon.large), 1), () -> tile.entity != null && tile.entity.items != null && tile.entity.items.has(item))));

        table.add(image).size(8*4);
    }

    @Override
    public String getIcon(){
        return "icon-item";
    }

    @Override
    public void update(Block block, TileEntity entity){

    }

    @Override
    public void trigger(Block block, TileEntity entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items != null && entity.items.has(item) && this.filter.test(item)){
                entity.items.remove(item, 1);
                break;
            }
        }
    }

    @Override
    public boolean valid(Block block, TileEntity entity){
        for(int i = 0; i < content.items().size; i++){
            Item item = content.item(i);
            if(entity.items != null && entity.items.has(item) && this.filter.test(item)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void display(BlockStats stats){
        stats.add(boost ? BlockStat.boostItem : BlockStat.inputItem, new ItemFilterValue(filter));
    }
}
