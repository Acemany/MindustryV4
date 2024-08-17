package io.acemany.mindustryV4.content.blocks;

import com.badlogic.gdx.utils.ObjectMap;
import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.blocks.Floor;
import io.acemany.mindustryV4.world.blocks.OreBlock;

import static io.acemany.mindustryV4.Vars.content;

public class OreBlocks extends BlockList{
    private static final ObjectMap<Item, ObjectMap<Block, Block>> oreBlockMap = new ObjectMap<>();

    public static Block get(Block floor, Item item){
        if(!oreBlockMap.containsKey(item)) throw new IllegalArgumentException("Item '" + item + "' is not an ore!");
        if(!oreBlockMap.get(item).containsKey(floor))
            throw new IllegalArgumentException("Block '" + floor.name + "' does not support ores!");
        return oreBlockMap.get(item).get(floor);
    }

    @Override
    public void load(){

        for(Item item : content.items()){
            if(!item.genOre) continue;
            ObjectMap<Block, Block> map = new ObjectMap<>();
            oreBlockMap.put(item, map);

            for(Block block : content.blocks()){
                if(block instanceof Floor && ((Floor) block).hasOres){
                    map.put(block, new OreBlock(item, (Floor) block));
                }
            }
        }
    }
}
