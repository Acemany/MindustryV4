package mindustryV4.content;

import io.anuke.arc.graphics.Color;
import mindustryV4.game.ContentList;
import mindustryV4.type.ContentType;
import mindustryV4.type.Liquid;

public class Liquids implements ContentList{
    public static Liquid water, lava, oil, cryofluid;

    @Override
    public void load(){

        water = new Liquid("water", Color.valueOf("486acd")){{
            heatCapacity = 0.4f;
            effect = StatusEffects.wet;
        }};

        lava = new Liquid("lava", Color.valueOf("e37341")){{
            temperature = 1f;
            viscosity = 0.8f;
            effect = StatusEffects.melting;
        }};

        oil = new Liquid("oil", Color.valueOf("313131")){{
            viscosity = 0.7f;
            flammability = 1.2f;
            explosiveness = 1.2f;
            heatCapacity = 0.7f;
            effect = StatusEffects.tarred;
        }};

        cryofluid = new Liquid("cryofluid", Color.valueOf("6ecdec")){{
            heatCapacity = 0.9f;
            temperature = 0.25f;
            effect = StatusEffects.freezing;
        }};
    }
}
