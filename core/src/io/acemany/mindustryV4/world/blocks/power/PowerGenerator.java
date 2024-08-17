package io.acemany.mindustryV4.world.blocks.power;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.world.meta.BlockFlag;
import io.anuke.ucore.util.EnumSet;

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
