package io.acemany.mindustryV4.world.meta.values;

import io.acemany.mindustryV4.game.UnlockableContent;
import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.type.ItemStack;
import io.acemany.mindustryV4.ui.ItemDisplay;
import io.acemany.mindustryV4.world.meta.ContentStatValue;
import io.anuke.ucore.scene.ui.layout.Table;

public class ItemValue implements ContentStatValue{
    private final ItemStack item;

    public ItemValue(ItemStack item){
        this.item = item;
    }

    @Override
    public UnlockableContent[] getValueContent(){
        return new Item[]{item.item};
    }

    @Override
    public void display(Table table){
        table.add(new ItemDisplay(item.item, item.amount));
    }
}
