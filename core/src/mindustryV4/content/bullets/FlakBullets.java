package mindustryV4.content.bullets;

import mindustryV4.content.fx.BulletFx;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.entities.bullet.BulletType;
import mindustryV4.entities.bullet.FlakBulletType;
import mindustryV4.entities.effect.Lightning;
import mindustryV4.graphics.Palette;
import mindustryV4.game.ContentList;
import ucore.util.Mathf;

public class FlakBullets extends BulletList implements ContentList{
    public static BulletType plastic, explosive, surge;

    @Override
    public void load(){


        plastic = new FlakBulletType(4f, 5){
            {
                splashDamageRadius = 40f;
                fragBullet = ArtilleryBullets.plasticFrag;
                fragBullets = 4;
                hiteffect = BulletFx.plasticExplosion;
                frontColor = Palette.plastaniumFront;
                backColor = Palette.plastaniumBack;
            }
        };

        explosive = new FlakBulletType(4f, 5){
            {
                //default bullet type, no changes
            }
        };

        surge = new FlakBulletType(4f, 7){
            {
                splashDamage = 33f;
            }

            @Override
            public void despawned(Bullet b) {
                super.despawned(b);

                for (int i = 0; i < 2; i++) {
                    Lightning.create(b.getTeam(), Palette.surge, damage, b.x, b.y, Mathf.random(360f), 12);
                }
            }
        };
    }
}