package mindustryV4.entities.traits;

import mindustryV4.game.Team;
import ucore.entities.trait.PosTrait;
import ucore.entities.trait.SolidTrait;
import ucore.entities.trait.VelocityTrait;

/**
 * Base interface for targetable entities.
 */
public interface TargetTrait extends PosTrait, VelocityTrait{

    boolean isDead();

    Team getTeam();

    default float getTargetVelocityX(){
        if(this instanceof SolidTrait){
            return ((SolidTrait) this).getDeltaX();
        }
        return getVelocity().x;
    }

    default float getTargetVelocityY(){
        if(this instanceof SolidTrait){
            return ((SolidTrait) this).getDeltaY();
        }
        return getVelocity().y;
    }

    /**
     * Whether this entity is a valid target.
     */
    default boolean isValid(){
        return !isDead();
    }
}
