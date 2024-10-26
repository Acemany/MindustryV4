package mindustryV4.world.consumers;

import com.badlogic.gdx.utils.Array;
import mindustryV4.entities.TileEntity;
import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.values.ItemFilterValue;
import ucore.function.Predicate;
import ucore.scene.ui.layout.Table;
import static mindustryV4.Vars.*;

public class ConsumeItemFilter extends Consume{
    private final Predicate<Item> filter;

    public ConsumeItemFilter(Predicate<Item> item){
        this.filter = item;
    }

    @Override
    public void buildTooltip(Table table){
        Array<Item> list = new Array<>();

        for(Item item : content.items()){
            if(filter.test(item)) list.add(item);
        }

        for(int i = 0; i < list.size; i++){
            Item item = list.get(i);
            table.addImage(item.region).size(8 * 4).padRight(2).padLeft(2);
            if(i != list.size - 1){
                table.add("/");
            }
        }
    }

    @Override
    public String getIcon(){
        return "icon-item";
    }

    @Override
    public void update(Block block, TileEntity entity){

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
        stats.add(optional ? BlockStat.boostItem : BlockStat.inputItem, new ItemFilterValue(filter));
    }
}
