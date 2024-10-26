package mindustryV4.world.consumers;

import mindustryV4.entities.TileEntity;
import mindustryV4.type.ItemStack;
import mindustryV4.ui.ItemImage;
import mindustryV4.world.Block;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.values.ItemListValue;
import ucore.scene.ui.layout.Table;

public class ConsumeItems extends Consume{
    private ItemStack[] items;

    public ConsumeItems(ItemStack[] items){
        this.items = items;
    }

    public ItemStack[] getItems(){
        return items;
    }

    @Override
    public void buildTooltip(Table table){
        for(ItemStack stack : items){
            table.add(new ItemImage(stack)).size(8 * 4).padRight(5);
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
        return entity.items != null && entity.items.has(items);
    }

    @Override
    public void display(BlockStats stats){
        stats.add(optional ? BlockStat.boostItem : BlockStat.inputItems, new ItemListValue(items));
    }
}
