package io.acemany.mindustryV4.world.blocks.defense.turrets;

import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.entities.bullet.Bullet;
import io.acemany.mindustryV4.entities.bullet.BulletType;
import io.acemany.mindustryV4.type.AmmoType;
import io.acemany.mindustryV4.type.Liquid;
import io.acemany.mindustryV4.world.Tile;
import io.acemany.mindustryV4.world.consumers.ConsumeLiquidFilter;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Mathf;

import static io.acemany.mindustryV4.Vars.tilesize;

public class LaserTurret extends PowerTurret{
    protected float firingMoveFract = 0.25f;
    protected float shootDuration = 100f;

    public LaserTurret(String name){
        super(name);
        canOverdrive = false;

        consumes.remove(ConsumeLiquidFilter.class);
        consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.01f)).update(false);
    }

    @Override
    public void update(Tile tile) {
        super.update(tile);

        LaserTurretEntity entity = tile.entity();

        if(entity.bulletLife > 0 && entity.bullet != null){
            tr.trns(entity.rotation, size * tilesize / 2, 0f);
            entity.bullet.setRotation(entity.rotation);
            entity.bullet.set(tile.drawx() + tr.x, tile.drawy() + tr.y);
            entity.bullet.time(0f);
            entity.heat = 1f;
            entity.recoil = recoil;
            entity.bulletLife -= Timers.delta();
            if(entity.bulletLife <= 0f){
                entity.bullet = null;
            }
        }
    }

    @Override
    protected void updateShooting(Tile tile){
        LaserTurretEntity entity = tile.entity();

        if(entity.bulletLife > 0 && entity.bullet != null){
            return;
        }

        if(entity.reload >= reload && entity.cons.valid()){
            AmmoType type = peekAmmo(tile);

            shoot(tile, type);

            entity.reload = 0f;
        }else{
            Liquid liquid = entity.liquids.current();

            float used = Math.min(Math.min(entity.liquids.get(liquid), maxCoolantUsed * Timers.delta()), Math.max(0, ((reload - entity.reload) / coolantMultiplier) / liquid.heatCapacity));
            entity.reload += (used * liquid.heatCapacity) / liquid.heatCapacity;
            entity.liquids.remove(liquid, used);

            if(Mathf.chance(0.06 * used)){
                Effects.effect(coolEffect, tile.drawx() + Mathf.range(size * tilesize / 2f), tile.drawy() + Mathf.range(size * tilesize / 2f));
            }
        }
    }

    @Override
    protected void turnToTarget(Tile tile, float targetRot){
        LaserTurretEntity entity = tile.entity();

        entity.rotation = Angles.moveToward(entity.rotation, targetRot, rotatespeed * entity.delta() * (entity.bulletLife > 0f ? firingMoveFract : 1f));
    }

    @Override
    protected void bullet(Tile tile, BulletType type, float angle){
        LaserTurretEntity entity = tile.entity();

        entity.bullet = Bullet.create(type, tile.entity, tile.getTeam(), tile.drawx() + tr.x, tile.drawy() + tr.y, angle);
        entity.bulletLife = shootDuration;
    }

    @Override
    public TileEntity newEntity(){
        return new LaserTurretEntity();
    }

    class LaserTurretEntity extends TurretEntity{
        Bullet bullet;
        float bulletLife;
    }
}
