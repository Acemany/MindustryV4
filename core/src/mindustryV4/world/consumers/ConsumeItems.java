package mindustryV4.world.consumers;

import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.type.Item.Icon;
import mindustryV4.type.ItemStack;
import mindustryV4.ui.ItemImage;
import mindustryV4.ui.ReqImage;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.values.ItemListValue;

public class ConsumeItems extends Consume{
    private ItemStack[] items;

    public ConsumeItems(ItemStack[] items){
        this.items = items;
    }

    public ItemStack[] getItems(){
        return items;
    }

    @Override
    public void build(Tile tile, Table table){
        for(ItemStack stack : items){
            table.add(new ReqImage(new ItemImage(stack.item.icon(Icon.large), stack.amount), () -> tile.entity != null && tile.entity.items != null && tile.entity.items.has(stack.item, stack.amount))).size(8*4).padRight(5);
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
    public void trigger(Block block, TileEntity entity){
        for(ItemStack stack : items){
            entity.items.remove(stack);
        }
    }

    @Override
    public boolean valid(Block block, TileEntity entity){
        return entity.items != null && entity.items.has(items);
    }

    @Override
    public void display(BlockStats stats){
        stats.add(boost ? BlockStat.boostItem : BlockStat.inputItems, new ItemListValue(items));
    }
}
