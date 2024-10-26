package mindustryV4.entities.traits;

import ucore.entities.trait.DamageTrait;
import ucore.entities.trait.Entity;

public interface AbsorbTrait extends Entity, TeamTrait, DamageTrait{
    void absorb();

    default boolean canBeAbsorbed(){
        return true;
    }

    default float getShieldDamage(){
        return getDamage();
    }
}
