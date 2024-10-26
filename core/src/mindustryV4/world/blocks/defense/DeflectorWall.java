package mindustryV4.world.blocks.defense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mindustryV4.entities.TileEntity;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.game.Team;
import mindustryV4.world.Tile;
import ucore.core.Graphics;
import ucore.core.Timers;
import ucore.graphics.Draw;
import ucore.util.Mathf;
import ucore.util.Physics;

import static mindustryV4.Vars.tilesize;

public class DeflectorWall extends Wall{
    public static final float hitTime = 10f;

    protected float maxDamageDeflect = 10f;
    protected Rectangle rect = new Rectangle();
    protected Rectangle rect2 = new Rectangle();

    public DeflectorWall(String name){
        super(name);
    }

    @Override
    public void draw(Tile tile){
        super.draw(tile);

        DeflectorEntity entity = tile.entity();

        if(entity.hit < 0.0001f) return;

        Graphics.setAdditiveBlending();

        Draw.color(Color.WHITE);
        Draw.alpha(entity.hit * 0.5f);
        Draw.rect("blank", tile.drawx(), tile.drawy(), tilesize * size, tilesize * size);
        Draw.reset();

        entity.hit = Mathf.clamp(entity.hit - Timers.delta() / hitTime);

        Graphics.setNormalBlending();
    }

    @Override
    public void handleBulletHit(TileEntity entity, Bullet bullet){
        super.handleBulletHit(entity, bullet);

        //doesn't reflect powerful bullets
        if(bullet.getDamage() > maxDamageDeflect) return;

        float penX = Math.abs(entity.x - bullet.x), penY = Math.abs(entity.y - bullet.y);

        bullet.getHitbox(rect2);

        Vector2 position = Physics.raycastRect(bullet.x, bullet.y, bullet.x + bullet.getVelocity().x, bullet.y + bullet.getVelocity().y,
                rect.setCenter(entity.x, entity.y).setSize(size * tilesize + rect2.width + rect2.height));

        if(position != null){
            bullet.set(position.x, position.y);
        }

        if(penX > penY){
            bullet.getVelocity().x *= -1;
        }else{
            bullet.getVelocity().y *= -1;
        }

        bullet.updateVelocity();
        bullet.resetOwner(entity, Team.none);
        bullet.scaleTime(1f);
        bullet.supress();

        ((DeflectorEntity) entity).hit = 1f;
    }

    @Override
    public TileEntity newEntity(){
        return new DeflectorEntity();
    }

    public static class DeflectorEntity extends TileEntity{
        public float hit;
    }
}
