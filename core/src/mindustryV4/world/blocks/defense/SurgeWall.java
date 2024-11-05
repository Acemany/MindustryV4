package mindustryV4.world.blocks.defense;

import mindustryV4.entities.type.TileEntity;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.entities.effect.Lightning;
import mindustryV4.graphics.Pal;
import io.anuke.arc.math.Mathf;

public class SurgeWall extends Wall{
    protected float lightningChance = 0.05f;
    protected float lightningDamage = 15f;
    protected int lightningLength = 17;

    public SurgeWall(String name){
        super(name);
    }

    @Override
    public void handleBulletHit(TileEntity entity, Bullet bullet){
        super.handleBulletHit(entity, bullet);
        if(Mathf.chance(lightningChance)){
            Lightning.create(entity.getTeam(), Pal.surge, lightningDamage, bullet.x, bullet.y, bullet.rot() + 180f, lightningLength);
        }
    }
}
