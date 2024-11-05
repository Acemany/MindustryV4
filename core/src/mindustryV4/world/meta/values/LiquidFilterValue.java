package mindustryV4.world.meta.values;

import io.anuke.arc.collection.Array;
import mindustryV4.type.Liquid;
import mindustryV4.ui.LiquidDisplay;
import mindustryV4.world.meta.StatValue;
import io.anuke.arc.function.Predicate;
import io.anuke.arc.scene.ui.layout.Table;

import static mindustryV4.Vars.content;

public class LiquidFilterValue implements StatValue{
    private final Predicate<Liquid> filter;

    public LiquidFilterValue(Predicate<Liquid> filter){
        this.filter = filter;
    }

    @Override
    public void display(Table table){
        Array<Liquid> list = new Array<>();

        for(Liquid item : content.liquids()){
            if(!item.isHidden() && filter.test(item)) list.add(item);
        }

        for(int i = 0; i < list.size; i++){
            table.add(new LiquidDisplay(list.get(i))).padRight(5);

            if(i != list.size - 1){
                table.add("/");
            }
        }
    }
}
