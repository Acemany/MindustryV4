package mindustryV4.ui;

import io.anuke.arc.collection.ObjectIntMap;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.type.Item;
import mindustryV4.type.Item.Icon;
import mindustryV4.type.ItemType;

import java.text.NumberFormat;
import java.util.Locale;

import static mindustryV4.Vars.*;

public class ItemsDisplay extends Table{
    private static final NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());

    public ItemsDisplay(){
        rebuild();
    }

    public void rebuild(){
        clear();
        top().left();
        margin(0);

        table("flat", t -> {
            t.margin(10).marginLeft(15).marginTop(15f);
            ObjectIntMap<Item> items = data.items();
            for(Item item : content.items()){
                if(item.type == ItemType.material && data.isUnlocked(item)){
                    t.label(() -> format.format(items.get(item, 0))).left();
                    t.addImage(item.icon(Icon.medium)).size(8*3).padLeft(4).padRight(4);
                    t.add(item.localizedName()).color(Color.LIGHT_GRAY).left();
                    t.row();
                }
            }
        });
    }
}
