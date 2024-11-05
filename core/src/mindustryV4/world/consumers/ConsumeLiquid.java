package mindustryV4.world.consumers;

import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.type.Liquid;
import mindustryV4.ui.ReqImage;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.BlockStats;
import mindustryV4.world.meta.StatUnit;

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
    public void build(Tile tile, Table table){
        table.add(new ReqImage(liquid.getContentIcon(), () -> valid(tile.block(), tile.entity))).size(8*4);
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
        if(!boost){
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
