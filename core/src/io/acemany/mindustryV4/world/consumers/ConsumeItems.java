package io.acemany.mindustryV4.world.consumers;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.type.ItemStack;
import io.acemany.mindustryV4.ui.ItemImage;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.meta.BlockStat;
import io.acemany.mindustryV4.world.meta.BlockStats;
import io.acemany.mindustryV4.world.meta.values.ItemListValue;
import io.anuke.ucore.scene.ui.layout.Table;

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
