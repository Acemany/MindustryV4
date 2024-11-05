package mindustryV4.world.meta;

import io.anuke.arc.collection.OrderedMap;
import io.anuke.arc.function.Function;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.ui.Bar;

public class BlockBars{
    private OrderedMap<String, Function<TileEntity, Bar>> bars = new OrderedMap<>();

    public void add(String name,Function<TileEntity, Bar> sup){
        bars.put(name, sup);
    }

    public void remove(String name){
        if(!bars.containsKey(name)) throw new RuntimeException("No bar with name '" + name + "' found; current bars: " + bars.keys().toArray());
        bars.remove(name);
    }

    public Iterable<Function<TileEntity, Bar>> list(){
        return bars.values();
    }
}
