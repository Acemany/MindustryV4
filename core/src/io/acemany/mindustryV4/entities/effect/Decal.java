package io.acemany.mindustryV4.entities.effect;

import com.badlogic.gdx.graphics.Color;
import io.acemany.mindustryV4.entities.traits.BelowLiquidTrait;
import io.anuke.ucore.entities.EntityGroup;
import io.anuke.ucore.entities.impl.TimedEntity;
import io.anuke.ucore.entities.trait.DrawTrait;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.util.Mathf;

import static io.acemany.mindustryV4.Vars.groundEffectGroup;

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
