package mindustryV4.world.consumers;

import io.anuke.arc.collection.Array;
import io.anuke.arc.function.Predicate;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.type.Liquid;
import mindustryV4.ui.MultiReqImage;
import mindustryV4.ui.ReqImage;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.StatUnit;
import mindustryV4.world.meta.values.LiquidFilterValue;

import static mindustryV4.Vars.content;

public class ConsumeLiquidFilter extends Consume{
    private final Predicate<Liquid> filter;
    private final float use;
    private final boolean isFuel;

    public ConsumeLiquidFilter(Predicate<Liquid> liquid, float amount, boolean isFuel){
        this.filter = liquid;
        this.use = amount;
        this.isFuel = isFuel;
    }

    public ConsumeLiquidFilter(Predicate<Liquid> liquid, float amount){
        this(liquid, amount, false);
    }

    @Override
    public void build(Tile tile, Table table){
        Array<Liquid> list = content.liquids().select(l -> !l.isHidden() && filter.test(l));
        MultiReqImage image = new MultiReqImage();
        list.each(liquid -> image.add(new ReqImage(liquid.getContentIcon(), () -> tile.entity != null && tile.entity.liquids != null && tile.entity.liquids.get(liquid) >= use(tile.block(), tile.entity))));

        table.add(image).size(8*4);
    }

    @Override
    public String getIcon(){
        return "icon-liquid-small";
    }

    @Override
    public void update(Block block, TileEntity entity){
        entity.liquids.remove(entity.liquids.current(), use(block, entity));
    }

    @Override
    public boolean valid(Block block, TileEntity entity){
        return entity != null && entity.liquids != null && filter.test(entity.liquids.current()) && entity.liquids.currentAmount() >= use(block, entity);
    }

    @Override
    public void display(BlockStats stats){
        if(boost){
            stats.add(BlockStat.boostLiquid, new LiquidFilterValue(filter));
        }else if(isFuel){
            stats.add(BlockStat.inputLiquidFuel, new LiquidFilterValue(filter));
            stats.add(BlockStat.liquidFuelUse, 60f * use, StatUnit.liquidSecond);
        }else{
            stats.add(BlockStat.inputLiquid, new LiquidFilterValue(filter));
            stats.add(BlockStat.liquidUse, 60f * use, StatUnit.liquidSecond);
        }
    }

    float use(Block block, TileEntity entity){
        return Math.min(use * entity.delta(), block.liquidCapacity);
    }
}
