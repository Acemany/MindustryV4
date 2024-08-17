package io.acemany.mindustryV4.world.blocks.defense.turrets;

import io.acemany.mindustryV4.type.AmmoType;
import io.acemany.mindustryV4.world.Tile;
import io.anuke.ucore.util.Mathf;

import static io.acemany.mindustryV4.Vars.tilesize;

public class DoubleTurret extends ItemTurret{
    protected float shotWidth = 2f;

    public DoubleTurret(String name){
        super(name);
        shots = 2;
    }

    @Override
    protected void shoot(Tile tile, AmmoType ammo){
        TurretEntity entity = tile.entity();
        entity.shots++;

        int i = Mathf.signs[entity.shots % 2];

        tr.trns(entity.rotation - 90, shotWidth * i, size * tilesize / 2);
        bullet(tile, ammo.bullet, entity.rotation + Mathf.range(inaccuracy));

        effects(tile);
        useAmmo(tile);
    }
}
