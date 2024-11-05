package mindustryV4.content;

import mindustryV4.game.ContentList;
import mindustryV4.type.Weapon;

public class Weapons implements ContentList{
    public static Weapon blaster, blasterSmall, glaiveBlaster, droneBlaster, healBlaster, healBlasterDrone, chainBlaster, shockgun,
    swarmer, bomber, bomberTrident, /*flakgun, */flamethrower, missiles, artillery, laserBurster, healBlasterDrone2;

    @Override
    public void load(){

        blaster = new Weapon("blaster"){{
            length = 1.5f;
            reload = 14f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardMechSmall;
        }};

        blasterSmall = new Weapon("blaster"){{
            length = 1.5f;
            reload = 20f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardCopper;
        }};

        glaiveBlaster = new Weapon("bomber"){{
            length = 1.5f;
            reload = 13f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardGlaive;
        }};

        droneBlaster = new Weapon("blaster"){{
            length = 2f;
            reload = 25f;
            width = 1f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardCopper;
        }};

        healBlaster = new Weapon("heal-blaster"){{
            length = 1.5f;
            reload = 24f;
            roundrobin = false;
            ejectEffect = Fx.none;
            recoil = 2f;
            bullet = Bullets.healBullet;
        }};

        missiles = new Weapon("missiles"){{
            length = 1.5f;
            reload = 70f;
            shots = 4;
            inaccuracy = 2f;
            roundrobin = true;
            ejectEffect = Fx.none;
            velocityRnd = 0.2f;
            spacing = 1f;
            bullet = Bullets.missileExplosive;
        }};

        swarmer = new Weapon("swarmer"){{
            length = 1.5f;
            recoil = 4f;
            reload = 60f;
            shots = 4;
            spacing = 8f;
            inaccuracy = 8f;
            roundrobin = true;
            ejectEffect = Fx.none;
            shake = 3f;
            bullet = Bullets.missileSwarm;
        }};

        chainBlaster = new Weapon("chain-blaster"){{
            length = 1.5f;
            reload = 28f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardCopper;
        }};

        shockgun = new Weapon("shockgun"){{
            length = 1f;
            reload = 40f;
            roundrobin = true;
            shots = 1;
            inaccuracy = 0f;
            velocityRnd = 0.2f;
            ejectEffect = Fx.none;
            bullet = Bullets.arc;
        }};

        /*flakgun = new Weapon("flakgun"){{
            length = 1f;
            reload = 70f;
            roundrobin = true;
            shots = 1;
            inaccuracy = 3f;
            recoil = 3f;
            velocityRnd = 0.1f;
            ejectEffect = Fx.shellEjectMedium;
            bullet = Bullets.shellCarbide;
        }};*/

        flamethrower = new Weapon("flamethrower"){{
            length = 1f;
            reload = 14f;
            roundrobin = true;
            recoil = 1f;
            ejectEffect = Fx.none;
            bullet = Bullets.basicFlame;
        }};

        artillery = new Weapon("artillery"){{
            length = 1f;
            reload = 60f;
            roundrobin = true;
            recoil = 5f;
            shake = 2f;
            ejectEffect = Fx.shellEjectMedium;
            bullet = Bullets.artilleryUnit;
        }};

        /*sapper = new Weapon("sapper"){{
            length = 1.5f;
            reload = 12f;
            roundrobin = true;
            ejectEffect = Fx.shellEjectSmall;
            bullet = Bullets.standardDense;
        }};*/

        bomber = new Weapon("bomber"){{
            length = 0f;
            width = 2f;
            reload = 12f;
            roundrobin = true;
            ejectEffect = Fx.none;
            velocityRnd = 1f;
            inaccuracy = 40f;
            bullet = Bullets.bombExplosive;
        }};

        bomberTrident = new Weapon("bomber"){{
            length = 0f;
            width = 2f;
            reload = 8f;
            shots = 2;
            roundrobin = true;
            ejectEffect = Fx.none;
            velocityRnd = 1f;
            inaccuracy = 40f;
            bullet = Bullets.bombExplosive;
        }};

        laserBurster = new Weapon("bomber"){{
            reload = 80f;
            shake = 3f;
            width = 0f;
            roundrobin = true;
            ejectEffect = Fx.none;
            bullet = Bullets.lancerLaser;
        }};

        healBlasterDrone = new Weapon("heal-blaster"){{
            length = 1.5f;
            reload = 40f;
            width = 0.5f;
            roundrobin = true;
            ejectEffect = Fx.none;
            recoil = 2f;
            bullet = Bullets.healBullet;
        }};

        healBlasterDrone2 = new Weapon("heal-blaster"){{
            length = 1.5f;
            reload = 20f;
            width = 0.5f;
            roundrobin = true;
            ejectEffect = Fx.none;
            recoil = 2f;
            bullet = Bullets.healBullet;
        }};
    }
}
