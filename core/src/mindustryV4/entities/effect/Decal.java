package mindustryV4.entities.effect;

import com.badlogic.gdx.graphics.Color;
import mindustryV4.entities.traits.BelowLiquidTrait;
import ucore.entities.EntityGroup;
import ucore.entities.impl.TimedEntity;
import ucore.entities.trait.DrawTrait;
import ucore.graphics.Draw;
import ucore.util.Mathf;

import static mindustryV4.Vars.groundEffectGroup;

/**
 * Class for creating block rubble on the ground.
 */
public abstract class Decal extends TimedEntity implements BelowLiquidTrait, DrawTrait{
    private static final Color color = Color.valueOf("52504e");

    @Override
    public float lifetime(){
        return 8200f;
    }

    @Override
    public void draw(){
        Draw.color(color.r, color.g, color.b, 1f - Mathf.curve(fin(), 0.98f));
        drawDecal();
        Draw.color();
    }

    @Override
    public EntityGroup targetGroup(){
        return groundEffectGroup;
    }

    abstract void drawDecal();
}
