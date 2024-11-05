package mindustryV4.world.blocks.defense.turrets;

import io.anuke.arc.math.Mathf;
import io.anuke.arc.util.Time;
import mindustryV4.entities.bullet.BulletType;
import mindustryV4.world.Tile;

import static mindustryV4.Vars.tilesize;

public class BurstTurret extends ItemTurret{
    protected float burstSpacing = 5;

    public BurstTurret(String name){
        super(name);
    }

    @Override
    protected void shoot(Tile tile, BulletType ammo){
        TurretEntity entity = tile.entity();

        entity.heat = 1f;

        for(int i = 0; i < shots; i++){
            Time.run(burstSpacing * i, () -> {
                if(!(tile.entity instanceof TurretEntity) ||
                        !hasAmmo(tile)) return;

                entity.recoil = recoil;

                tr.trns(entity.rotation, size * tilesize / 2, Mathf.range(xRand));
                bullet(tile, ammo, entity.rotation + Mathf.range(inaccuracy));
                effects(tile);
                useAmmo(tile);
            });
        }
    }
}
