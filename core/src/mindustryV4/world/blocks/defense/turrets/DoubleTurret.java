package mindustryV4.world.blocks.defense.turrets;

import io.anuke.arc.math.Mathf;
import mindustryV4.entities.bullet.BulletType;
import mindustryV4.world.Tile;

import static mindustryV4.Vars.tilesize;

public class DoubleTurret extends ItemTurret{
    protected float shotWidth = 2f;

    public DoubleTurret(String name){
        super(name);
        shots = 2;
    }

    @Override
    protected void shoot(Tile tile, BulletType ammo){
        TurretEntity entity = tile.entity();
        entity.shots++;

        int i = Mathf.signs[entity.shots % 2];

        tr.trns(entity.rotation - 90, shotWidth * i, size * tilesize / 2);
        bullet(tile, ammo, entity.rotation + Mathf.range(inaccuracy));

        effects(tile);
        useAmmo(tile);
    }
}
