package mindustryV4.ui;

import mindustryV4.type.Item;
import mindustryV4.type.ItemStack;
import io.anuke.arc.scene.ui.layout.Table;

/**An item image with text.*/
public class ItemDisplay extends Table{

    public ItemDisplay(Item item){
        this(item, 0);
    }

    public ItemDisplay(Item item, int amount){
        add(new ItemImage(new ItemStack(item, amount))).size(8*3);
        add(item.localizedName()).padLeft(4);
    }
}
