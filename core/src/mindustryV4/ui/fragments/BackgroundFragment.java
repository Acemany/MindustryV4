package mindustryV4.ui.fragments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mindustryV4.core.GameState.State;
import mindustryV4.graphics.Palette;
import mindustryV4.graphics.Shaders;
import ucore.core.Core;
import ucore.core.Graphics;
import ucore.graphics.Draw;
import ucore.graphics.Fill;
import ucore.scene.Group;
import ucore.scene.ui.layout.Unit;

import static mindustryV4.Vars.state;

public class BackgroundFragment extends Fragment{

    @Override
    public void build(Group parent){

        Core.scene.table().addRect((a, b, w, h) -> {
            Draw.colorl(0.1f);
            Fill.crect(0, 0, w, h);
            Draw.color(Palette.accent);
            Graphics.shader(Shaders.menu);
            Fill.crect(0, 0, w, h);
            Graphics.shader();
            Draw.color();

            boolean portrait = Gdx.graphics.getWidth() < Gdx.graphics.getHeight();
            float logoscl = (int) Unit.dp.scl(7) * (portrait ? 5f / 7f : 1f);
            TextureRegion logo = Core.skin.getRegion("logotext");
            float logow = logo.getRegionWidth() * logoscl;
            float logoh = logo.getRegionHeight() * logoscl;

            Draw.color();
            Core.batch.draw(logo, (int) (w / 2 - logow / 2), (int) (h - logoh + 15 - Unit.dp.scl(portrait ? 30f : 0)), logow, logoh);
        }).visible(() -> state.is(State.menu)).grow();
    }
}
