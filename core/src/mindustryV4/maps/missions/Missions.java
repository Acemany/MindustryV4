package mindustryV4.maps.missions;

import io.anuke.arc.collection.Array;
import mindustryV4.type.ItemStack;
import mindustryV4.world.Block;

public class Missions{

    /**Returns an array of missions to obtain the items needed to make this block.
     * This array includes a mission to place this block.*/
    public static Array<Mission> blockRecipe(Block block){
        ItemStack[] recipe = block.buildRequirements;

        Array<Mission> out = new Array<>();
        for(ItemStack stack : recipe){
            out.add(new ItemMission(stack.item, stack.amount));
        }
        out.add(new BlockMission(block));
        return out;
    }
}
