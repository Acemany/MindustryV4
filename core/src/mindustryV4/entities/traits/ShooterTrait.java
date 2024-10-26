package mindustryV4.entities.traits;

import mindustryV4.type.Weapon;
import ucore.entities.trait.VelocityTrait;
import ucore.util.Timer;

public interface ShooterTrait extends VelocityTrait, TeamTrait, InventoryTrait{

    Timer getTimer();

    int getShootTimer(boolean left);

    Weapon getWeapon();
}
