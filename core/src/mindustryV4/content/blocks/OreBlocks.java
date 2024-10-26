package mindustryV4.content.blocks;

import com.badlogic.gdx.utils.ObjectMap;
import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.blocks.Floor;
import mindustryV4.world.blocks.OreBlock;

import static mindustryV4.Vars.content;

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
