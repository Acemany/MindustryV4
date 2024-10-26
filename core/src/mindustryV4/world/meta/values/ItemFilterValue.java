package mindustryV4.world.meta.values;

import com.badlogic.gdx.utils.Array;
import mindustryV4.type.Item;
import mindustryV4.ui.ItemDisplay;
import mindustryV4.world.meta.StatValue;
import ucore.function.Predicate;
import ucore.scene.ui.layout.Table;

import static mindustryV4.Vars.content;

public class ItemFilterValue implements StatValue{
    private final Predicate<Item> filter;

    public ItemFilterValue(Predicate<Item> filter){
        this.filter = filter;
    }

    @Override
    public void display(Table table){
        Array<Item> list = new Array<>();

        for(Item item : content.items()){
            if(filter.test(item)) list.add(item);
        }

        for(int i = 0; i < list.size; i++){
            Item item = list.get(i);

            table.add(new ItemDisplay(item)).padRight(5);

            if(i != list.size - 1){
                table.add("/");
            }
        }
    }
}
