package mindustryV4.entities.bullet;

import com.badlogic.gdx.graphics.Color;
import mindustryV4.content.fx.BulletFx;
import mindustryV4.graphics.Palette;
import ucore.core.Effects;
import ucore.core.Timers;
import ucore.util.Mathf;

public class MissileBulletType extends BasicBulletType{
    protected Color trailColor = Palette.missileYellowBack;

    public MissileBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
        backColor = Palette.missileYellowBack;
        frontColor = Palette.missileYellow;
        homingPower = 7f;
    }

    @Override
    public void update(Bullet b){
        super.update(b);

        if(Mathf.chance(Timers.delta() * 0.2)){
            Effects.effect(BulletFx.missileTrail, trailColor, b.x, b.y, 2f);
        }
    }
}
