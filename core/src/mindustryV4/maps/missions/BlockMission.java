package mindustryV4.maps.missions;

import mindustryV4.type.Recipe;
import mindustryV4.world.Block;

/**A mission in which the player must place a block somewhere.*/
public class BlockMission extends ContentMission{

    public BlockMission(Block block) {
        super(Recipe.getByResult(block));
    }
}
