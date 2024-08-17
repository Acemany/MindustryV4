package io.acemany.mindustryV4.world.consumers;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.type.Liquid;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.meta.BlockStat;
import io.acemany.mindustryV4.world.meta.BlockStats;
import io.acemany.mindustryV4.world.meta.StatUnit;
import io.anuke.ucore.scene.ui.layout.Table;

public class ConsumeLiquid extends Consume{
    protected final float use;
    protected final Liquid liquid;

    public ConsumeLiquid(Liquid liquid, float use){
        this.liquid = liquid;
        this.use = use;
    }

    public float used(){
        return use;
    }

    public Liquid get(){
        return liquid;
    }

    @Override
    public void buildTooltip(Table table){
        table.addImage(liquid.getContentIcon()).size(8 * 3);
    }

    @Override
    public String getIcon(){
        return "icon-liquid-small";
    }

    @Override
    public void update(Block block, TileEntity entity){
        entity.liquids.remove(liquid, Math.min(use(block, entity), entity.liquids.get(liquid)));
    }

    @Override
    public boolean valid(Block block, TileEntity entity){
        return entity != null && entity.liquids != null && entity.liquids.get(liquid) >= use(block, entity);
    }

    @Override
    public void display(BlockStats stats){
        if(!optional){
            stats.add(BlockStat.liquidUse, use * 60f, StatUnit.liquidSecond);
            stats.add(BlockStat.inputLiquid, liquid);
        }else{
            stats.add(BlockStat.boostLiquid, liquid);
        }
    }

    float use(Block block, TileEntity entity){
        return Math.min(use * entity.delta(), block.liquidCapacity);
    }
}
