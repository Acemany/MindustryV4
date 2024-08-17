package io.acemany.mindustryV4.entities.traits;

import io.acemany.mindustryV4.type.Weapon;
import io.anuke.ucore.entities.trait.VelocityTrait;
import io.anuke.ucore.util.Timer;

public interface ShooterTrait extends VelocityTrait, TeamTrait, InventoryTrait{

    Timer getTimer();

    int getShootTimer(boolean left);

    Weapon getWeapon();
}
