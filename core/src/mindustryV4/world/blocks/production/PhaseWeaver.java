package mindustryV4.world.blocks.production;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mindustryV4.Vars;
import mindustryV4.graphics.Palette;
import mindustryV4.graphics.Shaders;
import mindustryV4.world.Tile;
import ucore.core.Graphics;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.util.Mathf;

public class PhaseWeaver extends PowerSmelter{
    protected TextureRegion bottomRegion;
    protected TextureRegion weaveRegion;

    public PhaseWeaver(String name){
        super(name);
    }

    @Override
    public void load(){
        super.load();

        bottomRegion = Draw.region(name + "-bottom");
        weaveRegion = Draw.region(name + "-weave");
    }

    @Override
    public TextureRegion[] getIcon(){
        if(icon == null){
            icon = new TextureRegion[]{Draw.region(name + "-bottom"), Draw.region(name)};
        }
        return icon;
    }

    @Override
    public void draw(Tile tile){
        PowerSmelterEntity entity = tile.entity();

        Draw.rect(bottomRegion, tile.drawx(), tile.drawy());

        float progress = 0.5f;

        Shaders.build.region = weaveRegion;
        Shaders.build.progress = progress;
        Shaders.build.color.set(Palette.accent);
        Shaders.build.color.a = entity.heat;
        Shaders.build.time = -entity.time / 10f;

        Graphics.shader(Shaders.build, false);
        Shaders.build.apply();
        Draw.rect(weaveRegion, tile.drawx(), tile.drawy(), entity.time);
        Graphics.shader();

        Draw.color(Palette.accent);
        Draw.alpha(entity.heat);

        Lines.lineAngleCenter(
                tile.drawx() + Mathf.sin(entity.time, 6f, Vars.tilesize / 3f * size),
                tile.drawy(),
                90,
                size * Vars.tilesize / 2f);

        Draw.reset();

        Draw.rect(region, tile.drawx(), tile.drawy());
    }
}
