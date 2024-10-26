package mindustryV4.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mindustryV4.type.ItemStack;
import ucore.function.Supplier;
import ucore.scene.ui.Image;
import ucore.scene.ui.layout.Stack;
import ucore.scene.ui.layout.Table;

public class ItemImage extends Stack{

    public ItemImage(TextureRegion region, Supplier<CharSequence> text){
        Table t = new Table().left().bottom();
        t.label(text);

        add(new Image(region));
        add(t);
    }

    public ItemImage(ItemStack stack){
        add(new Image(stack.item.region));

        if(stack.amount != 0){
            Table t = new Table().left().bottom();
            t.add(stack.amount + "");
            add(t);
        }
    }
}
