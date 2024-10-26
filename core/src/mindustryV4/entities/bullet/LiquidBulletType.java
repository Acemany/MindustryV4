package mindustryV4.entities.bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import mindustryV4.content.fx.BulletFx;
import mindustryV4.content.fx.Fx;
import mindustryV4.entities.effect.Fire;
import mindustryV4.entities.effect.Puddle;
import mindustryV4.type.Liquid;
import mindustryV4.world.Tile;
import ucore.core.Effects;
import ucore.graphics.Draw;
import ucore.graphics.Fill;
import ucore.util.Geometry;
import ucore.util.Mathf;

import static mindustryV4.Vars.tilesize;
import static mindustryV4.Vars.world;

public class LiquidBulletType extends BulletType{
    Liquid liquid;

    public LiquidBulletType(Liquid liquid){
        super(2.5f, 0);
        this.liquid = liquid;

        lifetime = 70f;
        despawneffect = Fx.none;
        hiteffect = BulletFx.hitLiquid;
        drag = 0.01f;
        knockback = 0.5f;
    }

    @Override
    public void update(Bullet b) {
        super.update(b);

        if(liquid.canExtinguish()){
            Tile tile = world.tileWorld(b.x, b.y);
            if(tile != null && Fire.has(tile.x, tile.y)){
                Fire.extinguish(tile, 100f);
                b.remove();
                hit(b);
            }
        }
    }

    @Override
    public void draw(Bullet b){
        Draw.color(liquid.color, Color.WHITE, b.fout() / 100f + Mathf.randomSeedRange(b.id, 0.1f));

        Fill.circle(b.x, b.y, 0.5f + b.fout() * 2.5f);
    }

    @Override
    public void hit(Bullet b, float hitx, float hity){
        Effects.effect(hiteffect, liquid.color, hitx, hity);
        Puddle.deposit(world.tileWorld(hitx, hity), liquid, 5f);

        if(liquid.temperature <= 0.5f && liquid.flammability < 0.3f){
            float intensity = 400f;
            Fire.extinguish(world.tileWorld(hitx, hity), intensity);
            for(GridPoint2 p : Geometry.d4){
                Fire.extinguish(world.tileWorld(hitx + p.x * tilesize, hity + p.y * tilesize), intensity);
            }
        }
    }
}
