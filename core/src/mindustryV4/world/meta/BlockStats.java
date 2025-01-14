package mindustryV4.world.meta;

import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import mindustryV4.type.Item;
import mindustryV4.type.ItemStack;
import mindustryV4.type.Liquid;
import mindustryV4.world.Block;
import mindustryV4.world.meta.values.*;
import ucore.util.Bundles;
import ucore.util.Log;

import java.util.Locale;

/**Hold and organizes a list of block stats.*/
public class BlockStats{
    private static final boolean errorWhenMissing = false;

    private final OrderedMap<StatCategory, OrderedMap<BlockStat, StatValue>> map = new OrderedMap<>();
    private final Block block;
    private boolean dirty;

    public BlockStats(Block block){
        this.block = block;
    }

    /**Adds a single float value with this stat, formatted to 2 decimal places.*/
    public void add(BlockStat stat, float value, StatUnit unit){
        add(stat, new NumberValue(value, unit));
    }

    /**Adds a single y/n boolean value.*/
    public void add(BlockStat stat, boolean value){
        add(stat, new BooleanValue(value));
    }

    /**Adds an item value.*/
    public void add(BlockStat stat, Item item){
        add(stat, new ItemValue(new ItemStack(item, 1)));
    }

    /**Adds a liquid value.*/
    public void add(BlockStat stat, Liquid liquid){
        add(stat, new LiquidValue(liquid));
    }

    /**Adds an item value.*/
    public void add(BlockStat stat, ItemStack item){
        add(stat, new ItemValue(item));
    }

    /**Adds a single string value with this stat.*/
    public void add(BlockStat stat, String format, Object... args){
        add(stat, new StringValue(format, args));
    }

    /**Adds a stat value.*/
    public void add(BlockStat stat, StatValue value){
        if(!Bundles.has("blocks." + stat.name().toLowerCase(Locale.ROOT))){
            if(!errorWhenMissing){
                Log.err("Warning: No bundle entry for stat type \"" + stat + "\"!");
            }else{
                throw new RuntimeException("No bundle entry for stat type \"" + stat + "\"!");
            }
        }

        if(!Bundles.has("category." + stat.category.name().toLowerCase(Locale.ROOT))){
            if(!errorWhenMissing){
                Log.err("Warning: No bundle entry for stat category \"" + stat.category + "\"!");
            }else{
                throw new RuntimeException("No bundle entry for stat category \"" + stat.category + "\"!");
            }
        }

        if(map.containsKey(stat.category) && map.get(stat.category).containsKey(stat)){
            throw new RuntimeException("Duplicate stat entry: \"" + stat + "\" in block '" + block.name + "'");
        }

        if(!map.containsKey(stat.category)){
            map.put(stat.category, new OrderedMap<>());
        }

        map.get(stat.category).put(stat, value);

        dirty = true;
    }

    /**Removes a stat, if it exists.*/
    public void remove(BlockStat stat){
        if(!map.containsKey(stat.category) || !map.get(stat.category).containsKey(stat)){
            throw new RuntimeException("No stat entry found: \"" + stat + "\" in block '" + block.name + "'!");
        }

        map.get(stat.category).remove(stat);

        dirty = true;
    }

    public OrderedMap<StatCategory, OrderedMap<BlockStat, StatValue>> toMap(){
        //sort stats by index if they've been modified
        if(dirty){
            map.orderedKeys().sort();
            for(Entry<StatCategory, OrderedMap<BlockStat, StatValue>> entry : map.entries()){
                entry.value.orderedKeys().sort();
            }

            dirty = false;
        }
        return map;
    }
}
