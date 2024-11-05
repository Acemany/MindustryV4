package mindustryV4.world.meta.values;

import mindustryV4.game.UnlockableContent;
import mindustryV4.type.Item;
import mindustryV4.type.ItemStack;
import mindustryV4.ui.ItemDisplay;
import mindustryV4.world.meta.ContentStatValue;
import io.anuke.arc.scene.ui.layout.Table;

public class ItemListValue implements ContentStatValue{
    private final Item[] items;
    private final ItemStack[] stacks;

    public ItemListValue(Item[] items){
        this.items = items;
        this.stacks = null;
    }

    public ItemListValue(ItemStack[] stacks){
        this.stacks = stacks;
        this.items = null;
    }

    @Override
    public UnlockableContent[] getValueContent(){
        if(items != null){
            return items;
        }else{
            Item[] res = new Item[stacks.length];
            for(int i = 0; i < res.length; i++){
                res[i] = stacks[i].item;
            }
            return res;
        }
    }

    @Override
    public void display(Table table){
        if(items != null){
            for(Item item : items){
                table.add(new ItemDisplay(item)).padRight(5);
            }
        }else{
            for(ItemStack stack : stacks){
                table.add(new ItemDisplay(stack.item, stack.amount)).padRight(5);
            }
        }
    }
}
