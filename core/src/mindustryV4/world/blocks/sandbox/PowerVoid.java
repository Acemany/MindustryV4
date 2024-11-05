package mindustryV4.world.blocks.sandbox;

import mindustryV4.world.blocks.PowerBlock;
import mindustryV4.world.meta.BlockStat;

public class PowerVoid extends PowerBlock{

    public PowerVoid(String name){
        super(name);
        consumes.power(Float.MAX_VALUE);
    }

    @Override
    public void init(){
        super.init();
        stats.remove(BlockStat.powerUse);
    }
}
