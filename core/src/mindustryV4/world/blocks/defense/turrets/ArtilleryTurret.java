package mindustryV4.world.blocks.defense.turrets;

import com.badlogic.gdx.math.Vector2;
import mindustryV4.entities.Predict;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.type.AmmoType;
import mindustryV4.world.Tile;
import ucore.util.Mathf;

import static mindustryV4.Vars.tilesize;

/**
 * Artillery turrets have special shooting calculations done to hit targets.
 */
public class ArtilleryTurret extends ItemTurret{
    protected float velocityInaccuracy = 0f;

    public ArtilleryTurret(String name){
        super(name);
        targetAir = false;
    }

    @Override
    protected void shoot(Tile tile, AmmoType ammo){
        TurretEntity entity = tile.entity();

        entity.recoil = recoil;
        entity.heat = 1f;

        AmmoType type = peekAmmo(tile);

        tr.trns(entity.rotation, size * tilesize / 2);

        Vector2 predict = Predict.intercept(tile, entity.target, type.bullet.speed);

        float dst = entity.distanceTo(predict.x, predict.y);
        float maxTraveled = type.bullet.lifetime * type.bullet.speed;

        for(int i = 0; i < shots; i++){
            Bullet.create(ammo.bullet, tile.entity, tile.getTeam(), tile.drawx() + tr.x, tile.drawy() + tr.y,
            entity.rotation + Mathf.range(inaccuracy + type.inaccuracy), 1f + Mathf.range(velocityInaccuracy), (dst / maxTraveled));
        }

        effects(tile);
        useAmmo(tile);
    }
}
