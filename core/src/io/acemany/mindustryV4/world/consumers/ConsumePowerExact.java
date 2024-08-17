package io.acemany.mindustryV4.world.consumers;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.world.Block;

public class ConsumePowerExact extends ConsumePower{

    public ConsumePowerExact(float use){
        super(use);
    }

    @Override
    protected float use(Block block, TileEntity entity){
        return this.use;
    }
}
