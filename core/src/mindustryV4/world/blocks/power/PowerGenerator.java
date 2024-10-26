package mindustryV4.world.blocks.power;

import mindustryV4.entities.TileEntity;
import mindustryV4.world.meta.BlockFlag;
import ucore.util.EnumSet;

public class PowerGenerator extends PowerDistributor{

    public PowerGenerator(String name){
        super(name);
        baseExplosiveness = 5f;
        flags = EnumSet.of(BlockFlag.producer);
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public TileEntity newEntity(){
        return new GeneratorEntity();
    }

    public static class GeneratorEntity extends TileEntity{
        public float generateTime;
    }
}
