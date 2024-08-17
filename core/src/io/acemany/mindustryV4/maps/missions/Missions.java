package io.acemany.mindustryV4.maps.missions;

import com.badlogic.gdx.utils.Array;
import io.acemany.mindustryV4.type.ItemStack;
import io.acemany.mindustryV4.type.Recipe;
import io.acemany.mindustryV4.world.Block;

public class Missions{

    /**Returns an array of missions to obtain the items needed to make this block.
     * This array includes a mission to place this block.*/
    public static Array<Mission> blockRecipe(Block block){
        Recipe recipe = Recipe.getByResult(block);

        Array<Mission> out = new Array<>();
        for(ItemStack stack : recipe.requirements){
            out.add(new ItemMission(stack.item, stack.amount));
        }
        out.add(new BlockMission(block));
        return out;
    }
}
