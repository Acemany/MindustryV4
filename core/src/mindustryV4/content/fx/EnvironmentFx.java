package mindustryV4.content.fx;

import com.badlogic.gdx.graphics.Color;
import mindustryV4.content.Liquids;
import mindustryV4.game.ContentList;
import mindustryV4.graphics.Palette;
import mindustryV4.type.Item;
import ucore.core.Effects.Effect;
import ucore.graphics.Draw;
import ucore.graphics.Fill;
import ucore.util.Angles;
import ucore.util.Mathf;

public class EnvironmentFx extends FxList implements ContentList{
    public static Effect burning, fire, smoke, steam, fireballsmoke, ballfire, freezing, melting, wet, oily, overdriven, dropItem;

    @Override
    public void load(){

        burning = new Effect(35f, e -> {
            Draw.color(Palette.lightFlame, Palette.darkFlame, e.fin());

            Angles.randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.4f);
            });

            Draw.color();
        });

        fire = new Effect(35f, e -> {
            Draw.color(Palette.lightFlame, Palette.darkFlame, e.fin());

            Angles.randLenVectors(e.id, 2, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
            });

            Draw.color();
        });

        smoke = new Effect(35f, e -> {
            Draw.color(Color.GRAY);

            Angles.randLenVectors(e.id, 1, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
            });

            Draw.color();
        });

        steam = new Effect(35f, e -> {
            Draw.color(Color.LIGHT_GRAY);

            Angles.randLenVectors(e.id, 2, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fslope() * 1.5f);
            });

            Draw.color();
        });

        fireballsmoke = new Effect(25f, e -> {
            Draw.color(Color.GRAY);

            Angles.randLenVectors(e.id, 1, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fout() * 1.5f);
            });

            Draw.color();
        });

        ballfire = new Effect(25f, e -> {
            Draw.color(Palette.lightFlame, Palette.darkFlame, e.fin());

            Angles.randLenVectors(e.id, 2, 2f + e.fin() * 7f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, 0.2f + e.fout() * 1.5f);
            });

            Draw.color();
        });

        freezing = new Effect(40f, e -> {
            Draw.color(Liquids.cryofluid.color);

            Angles.randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout() * 1.2f);
            });

            Draw.color();
        });

        melting = new Effect(40f, e -> {
            Draw.color(Liquids.lava.color, Color.WHITE, e.fout() / 5f + Mathf.randomSeedRange(e.id, 0.12f));

            Angles.randLenVectors(e.id, 2, 1f + e.fin() * 3f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, .2f + e.fout() * 1.2f);
            });

            Draw.color();
        });

        wet = new Effect(40f, e -> {
            Draw.color(Liquids.water.color);

            Angles.randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout());
            });

            Draw.color();
        });

        oily = new Effect(42f, e -> {
            Draw.color(Liquids.oil.color);

            Angles.randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
                Fill.circle(e.x + x, e.y + y, e.fout());
            });

            Draw.color();
        });

        overdriven = new Effect(20f, e -> {
            Draw.color(Palette.accent);

            Angles.randLenVectors(e.id, 2, 1f + e.fin() * 2f, (x, y) -> {
                Fill.square(e.x + x, e.y + y, e.fout() * 2.3f+0.5f);
            });

            Draw.color();
        });

        dropItem = new Effect(20f, e -> {
            float length = 20f * e.finpow();
            float size = 7f * e.fout();

            Draw.rect(((Item) e.data).region, e.x + Angles.trnsx(e.rotation, length), e.y + Angles.trnsy(e.rotation, length), size, size);
        });
    }
}
