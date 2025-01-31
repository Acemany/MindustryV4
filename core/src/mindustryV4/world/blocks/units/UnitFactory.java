package mindustryV4.world.blocks.units;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import mindustryV4.Vars;
import mindustryV4.content.fx.BlockFx;
import mindustryV4.entities.TileEntity;
import mindustryV4.entities.units.BaseUnit;
import mindustryV4.entities.units.UnitType;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Palette;
import mindustryV4.graphics.Shaders;
import mindustryV4.net.Net;
import mindustryV4.type.Item;
import mindustryV4.type.ItemStack;
import mindustryV4.world.BarType;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.consumers.ConsumeItems;
import mindustryV4.world.meta.BlockBar;
import mindustryV4.world.meta.BlockFlag;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.StatUnit;
import mindustryV4.world.modules.ItemModule;
import ucore.core.Effects;
import ucore.core.Graphics;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.util.EnumSet;
import ucore.util.Mathf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class UnitFactory extends Block{
    protected float gracePeriodMultiplier = 15f;
    protected float speedupTime = 60f * 60f * 20;
    protected float maxSpeedup = 2f;

    protected UnitType type;
    protected float produceTime = 1000f;
    protected float launchVelocity = 0f;
    protected TextureRegion topRegion;

    public UnitFactory(String name){
        super(name);
        update = true;
        hasPower = true;
        hasItems = true;
        solid = false;
        itemCapacity = 10;
        flags = EnumSet.of(BlockFlag.producer, BlockFlag.target);

        consumes.require(ConsumeItems.class);
    }

    @Remote(called = Loc.server)
    public static void onUnitFactorySpawn(Tile tile){
        if(!(tile.entity instanceof UnitFactoryEntity) || !(tile.block() instanceof UnitFactory)) return;

        UnitFactoryEntity entity = tile.entity();
        UnitFactory factory = (UnitFactory) tile.block();

        entity.buildTime = 0f;

        Effects.shake(2f, 3f, entity);
        Effects.effect(BlockFx.producesmoke, tile.drawx(), tile.drawy());

        if(!Net.client()){
            BaseUnit unit = factory.type.create(tile.getTeam());
            unit.setSpawner(tile);
            unit.set(tile.drawx() + Mathf.range(4), tile.drawy() + Mathf.range(4));
            unit.add();
            unit.getVelocity().y = factory.launchVelocity;
        }
    }

    @Override
    public void load(){
        super.load();

        topRegion = Draw.region(name + "-top");
    }

    @Override
    public boolean outputsItems(){
        return false;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(BlockStat.craftSpeed, produceTime / 60f, StatUnit.seconds);
    }

    @Override
    public void setBars(){
        super.setBars();

        bars.add(new BlockBar(BarType.production, true, tile -> tile.<UnitFactoryEntity>entity().buildTime / produceTime));
        bars.remove(BarType.inventory);
    }

    @Override
    public TextureRegion[] getIcon(){
        return new TextureRegion[]{
            Draw.region(name),
            Draw.region(name + "-top")
        };
    }

    @Override
    public void draw(Tile tile){
        UnitFactoryEntity entity = tile.entity();
        TextureRegion region = type.iconRegion;

        Draw.rect(name(), tile.drawx(), tile.drawy());

        Shaders.build.region = region;
        Shaders.build.progress = entity.buildTime / produceTime;
        Shaders.build.color.set(Palette.accent);
        Shaders.build.color.a = entity.speedScl;
        Shaders.build.time = -entity.time / 10f;

        Graphics.shader(Shaders.build, false);
        Shaders.build.apply();
        Draw.rect(region, tile.drawx(), tile.drawy());
        Graphics.shader();

        Draw.color(Palette.accent);
        Draw.alpha(entity.speedScl);

        Lines.lineAngleCenter(
                tile.drawx() + Mathf.sin(entity.time, 6f, Vars.tilesize / 2f * size - 2f),
                tile.drawy(),
                90,
                size * Vars.tilesize - 4f);

        Draw.reset();

        Draw.rect(topRegion, tile.drawx(), tile.drawy());
    }

    @Override
    public void update(Tile tile){
        UnitFactoryEntity entity = tile.entity();

        entity.time += entity.delta() * entity.speedScl;

        if(tile.isEnemyCheat()){
            entity.warmup += entity.delta();
        }

        if(!tile.isEnemyCheat()){
            //player-made spawners have default behavior

            if(hasRequirements(entity.items, entity.buildTime / produceTime) && entity.cons.valid()){

                entity.buildTime += entity.delta();
                entity.speedScl = Mathf.lerpDelta(entity.speedScl, 1f, 0.05f);
            }else{
                entity.speedScl = Mathf.lerpDelta(entity.speedScl, 0f, 0.05f);
            }
            //check if grace period had passed
        }else if(entity.warmup > produceTime*gracePeriodMultiplier * Vars.state.difficulty.spawnerScaling){
            float speedMultiplier = Math.min(0.1f + (entity.warmup - produceTime * gracePeriodMultiplier * Vars.state.difficulty.spawnerScaling) / speedupTime, maxSpeedup);
            //otherwise, it's an enemy, cheat by not requiring resources
            entity.buildTime += entity.delta() * speedMultiplier;
            entity.speedScl = Mathf.lerpDelta(entity.speedScl, 1f, 0.05f);
        }else{
            entity.speedScl = Mathf.lerpDelta(entity.speedScl, 0f, 0.05f);
        }

        if(entity.buildTime >= produceTime){
            entity.buildTime = 0f;

            Call.onUnitFactorySpawn(tile);
            useContent(tile, type);

            for(ItemStack stack : consumes.items()){
                entity.items.remove(stack.item, stack.amount);
            }
        }
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        for(ItemStack stack : consumes.items()){
            if(item == stack.item && tile.entity.items.get(item) < stack.amount * 2){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaximumAccepted(Tile tile, Item item){
        for(ItemStack stack : consumes.items()){
            if(item == stack.item){
                return stack.amount * 2;
            }
        }
        return 0;
    }

    @Override
    public TileEntity newEntity(){
        return new UnitFactoryEntity();
    }

    protected boolean hasRequirements(ItemModule inv, float fraction){
        for(ItemStack stack : consumes.items()){
            if(!inv.has(stack.item, (int) (fraction * stack.amount))){
                return false;
            }
        }
        return true;
    }

    public static class UnitFactoryEntity extends TileEntity{
        public float buildTime;
        public float time;
        public float speedScl;
        public float warmup; //only for enemy spawners

        @Override
        public void write(DataOutput stream) throws IOException{
            stream.writeFloat(buildTime);
            stream.writeFloat(warmup);
        }

        @Override
        public void read(DataInput stream) throws IOException{
            buildTime = stream.readFloat();
            warmup = stream.readFloat();
        }
    }
}
