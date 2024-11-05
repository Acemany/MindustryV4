package mindustryV4.entities.bullet;

import mindustryV4.entities.Effects;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.Fill;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.math.geom.Geometry;
import io.anuke.arc.math.geom.Point2;
import mindustryV4.content.Fx;
import mindustryV4.entities.effect.Fire;
import mindustryV4.entities.effect.Puddle;
import mindustryV4.type.Liquid;
import mindustryV4.world.Tile;

import static mindustryV4.Vars.*;

public class LiquidBulletType extends BulletType{
    Liquid liquid;

    public LiquidBulletType(Liquid liquid){
        super(2.5f, 0);
        this.liquid = liquid;

        lifetime = 70f;
        status = liquid.effect;
        statusDuration = 90f;
        despawnEffect = Fx.none;
        hitEffect = Fx.hitLiquid;
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
        Effects.effect(hitEffect, liquid.color, hitx, hity);
        Puddle.deposit(world.tileWorld(hitx, hity), liquid, 5f);

        if(liquid.temperature <= 0.5f && liquid.flammability < 0.3f){
            float intensity = 400f;
            Fire.extinguish(world.tileWorld(hitx, hity), intensity);
            for(Point2 p : Geometry.d4){
                Fire.extinguish(world.tileWorld(hitx + p.x * tilesize, hity + p.y * tilesize), intensity);
            }
        }
    }
}
