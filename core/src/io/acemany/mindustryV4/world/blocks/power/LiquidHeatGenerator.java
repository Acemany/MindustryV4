package io.acemany.mindustryV4.world.blocks.power;

import io.acemany.mindustryV4.type.Liquid;
import io.acemany.mindustryV4.world.meta.BlockStat;
import io.acemany.mindustryV4.world.meta.StatUnit;

public class LiquidHeatGenerator extends LiquidGenerator{

    public LiquidHeatGenerator(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(BlockStat.basePowerGeneration, maxLiquidGenerate * powerPerLiquid * 60f, StatUnit.powerSecond);
    }

    @Override
    protected float getEfficiency(Liquid liquid){
        return liquid.temperature - 0.5f;
    }
}
