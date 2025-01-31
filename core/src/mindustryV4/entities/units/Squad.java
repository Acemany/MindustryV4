package mindustryV4.entities.units;

import com.badlogic.gdx.math.Vector2;
import ucore.util.Translator;

import static mindustryV4.Vars.threads;

/**
 * Used to group entities together, for formations and such.
 * Usually, squads are used by units spawned in the same wave.
 */
public class Squad{
    public Vector2 direction = new Translator();
    public int units;

    private long lastUpdated;

    protected void update(){
        if(threads.getFrameID() != lastUpdated){
            direction.setZero();
            lastUpdated = threads.getFrameID();
        }
    }
}
