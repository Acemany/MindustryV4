package io.acemany.mindustryV4.world.consumers;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.type.ItemStack;
import io.acemany.mindustryV4.ui.ItemImage;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.meta.BlockStat;
import io.acemany.mindustryV4.world.meta.BlockStats;
import io.anuke.ucore.scene.ui.layout.Table;

public class ConsumeItem extends Consume{
    private final Item item;
    private final int amount;

    public ConsumeItem(Item item){
        this.item = item;
        this.amount = 1;
    }

    public ConsumeItem(Item item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public int getAmount(){
        return amount;
    }

    public Item get(){
        return item;
    }

    @Override
    public void buildTooltip(Table table){
        table.add(new ItemImage(new ItemStack(item, amount))).size(8 * 4);
    }

    @Override
    public String getIcon(){
        return "icon-item";
    }

    @Override
    public void update(Block block, TileEntity entity){
        //doesn't update because consuming items is very specific
    }

    @Override
    public boolean valid(Block block, TileEntity entity){
        return entity != null && entity.items != null && entity.items.has(item, amount);
    }

    @Override
    public void display(BlockStats stats){
        stats.add(optional ? BlockStat.boostItem : BlockStat.inputItem, item);
    }
}
