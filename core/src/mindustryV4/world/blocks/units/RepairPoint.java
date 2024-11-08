package mindustryV4.world.blocks.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import mindustryV4.entities.TileEntity;
import mindustryV4.entities.Unit;
import mindustryV4.entities.Units;
import mindustryV4.graphics.Layer;
import mindustryV4.graphics.Palette;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockFlag;
import ucore.core.Timers;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.graphics.Shapes;
import ucore.util.Angles;
import ucore.util.EnumSet;
import ucore.util.Mathf;

public class RepairPoint extends Block{
    private static Rectangle rect = new Rectangle();

    protected int timerTarget = timers++;

    protected float repairRadius = 50f;
    protected float repairSpeed = 0.3f;

    protected TextureRegion topRegion;

    public RepairPoint(String name){
        super(name);
        update = true;
        solid = true;
        flags = EnumSet.of(BlockFlag.repair);
        layer = Layer.turret;
        layer2 = Layer.laser;
        hasPower = true;
        powerCapacity = 20f;
        consumes.power(0.06f);
    }

    @Override
    public void load(){
        super.load();

        topRegion = Draw.region(name + "-turret");
    }

    @Override
    public void drawSelect(Tile tile){
        Draw.color(Palette.accent);
        Lines.dashCircle(tile.drawx(), tile.drawy(), repairRadius);
        Draw.color();
    }

    @Override
    public void drawLayer(Tile tile){
        RepairPointEntity entity = tile.entity();

        Draw.rect(topRegion, tile.drawx(), tile.drawy(), entity.rotation - 90);
    }

    @Override
    public void drawLayer2(Tile tile){
        RepairPointEntity entity = tile.entity();

        if(entity.target != null &&
                Angles.angleDist(entity.angleTo(entity.target), entity.rotation) < 30f){
            float ang = entity.angleTo(entity.target);
            float len = 5f;

            Draw.color(Color.valueOf("e8ffd7"));
            Shapes.laser("laser", "laser-end",
                    tile.drawx() + Angles.trnsx(ang, len), tile.drawy() + Angles.trnsy(ang, len),
                    entity.target.x, entity.target.y, entity.strength);
            Draw.color();
        }
    }

    @Override
    public void update(Tile tile){
        RepairPointEntity entity = tile.entity();

        if(entity.target != null && (entity.target.isDead() || entity.target.distanceTo(tile) > repairRadius ||
                entity.target.health >= entity.target.maxHealth())){
            entity.target = null;
        }else if(entity.target != null){
            entity.target.health += repairSpeed * Timers.delta() * entity.strength;
            entity.target.clampHealth();
            entity.rotation = Mathf.slerpDelta(entity.rotation, entity.angleTo(entity.target), 0.5f);
        }

        if(entity.target != null && entity.cons.valid()){
            entity.strength = Mathf.lerpDelta(entity.strength, 1f, 0.08f * Timers.delta());
        }else{
            entity.strength = Mathf.lerpDelta(entity.strength, 0f, 0.07f * Timers.delta());
        }

        if(entity.timer.get(timerTarget, 20)){
            rect.setSize(repairRadius * 2).setCenter(tile.drawx(), tile.drawy());
            entity.target = Units.getClosest(tile.getTeam(), tile.drawx(), tile.drawy(), repairRadius,
                    unit -> unit.health < unit.maxHealth());
        }
    }

    @Override
    public boolean shouldConsume(Tile tile){
        RepairPointEntity entity = tile.entity();

        return entity.target != null;
    }

    @Override
    public TileEntity newEntity(){
        return new RepairPointEntity();
    }

    public class RepairPointEntity extends TileEntity{
        public Unit target;
        public float strength, rotation = 90;
    }
}
