package mindustryV4.world.meta.values;

import io.anuke.arc.collection.Array;
import io.anuke.arc.function.Predicate;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.type.Item;
import mindustryV4.ui.ItemDisplay;
import mindustryV4.world.meta.StatValue;

import static mindustryV4.Vars.content;

public class ItemFilterValue implements StatValue{
    private final Predicate<Item> filter;

    public ItemFilterValue(Predicate<Item> filter){
        this.filter = filter;
    }

    @Override
    public void display(Table table){
        Array<Item> list = content.items().select(filter);

        for(int i = 0; i < list.size; i++){
            Item item = list.get(i);

            table.add(new ItemDisplay(item)).padRight(5);

            if(i != list.size - 1){
                table.add("/");
            }
        }
    }
}
