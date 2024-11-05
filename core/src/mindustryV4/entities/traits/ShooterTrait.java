package mindustryV4.entities.traits;

import io.anuke.arc.util.Interval;
import mindustryV4.type.Weapon;

public interface ShooterTrait extends VelocityTrait, TeamTrait{

    Interval getTimer();

    int getShootTimer(boolean left);

    Weapon getWeapon();
}
