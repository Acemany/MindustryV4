package mindustryV4.ui;

import io.anuke.arc.Core;
import io.anuke.arc.graphics.Texture.*;
import io.anuke.arc.graphics.g2d.*;
import io.anuke.arc.scene.Element;
import io.anuke.arc.scene.event.InputEvent;
import io.anuke.arc.scene.event.InputListener;
import io.anuke.arc.scene.ui.layout.Container;
import mindustryV4.graphics.*;

import static mindustryV4.Vars.*;

public class Minimap extends Container<Element>{

    public Minimap(){
        super(new Element(){
            @Override
            public void draw(){
                if(renderer.minimap.getRegion() == null) return;

                Draw.rect(renderer.minimap.getRegion(), x + width/2f, y + height/2f, width, height);

                if(renderer.minimap.getTexture() != null){
                    renderer.minimap.drawEntities(x, y, width, height);
                }

                if(showFog){
                    renderer.fog.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

                    TextureRegion r = renderer.minimap.getRegion();
                    float pad = renderer.fog.getPadding();

                    float px = r.getU() * world.width() + pad;
                    float py = r.getV() * world.height() + pad;
                    float px2 = r.getU2() * world.width() + pad;
                    float py2 = r.getV2() * world.height() + pad;

                    r.setTexture(renderer.fog.getTexture());
                    r.setU(px / (world.width() + pad*2f));
                    r.setV(1f - py / (world.height() + pad*2f));
                    r.setU2(px2 / (world.width() + pad*2f));
                    r.setV2(1f - py2 / (world.height() + pad*2f));

                    Draw.shader(Shaders.fog);
                    Draw.rect(r, x, y, width, height);
                    Draw.shader();

                    renderer.fog.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                }
            }
        });

        background("pane");
        size(140f);
        margin(5f);

        addListener( new InputListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountx, float amounty){
                renderer.minimap.zoomBy(amounty);
                return true;
            }
        });

        update(() -> {

            Element e = Core.scene.hit(Core.input.mouseX(), Core.input.mouseY(), true);
            if(e != null && e.isDescendantOf(this)){
                Core.scene.setScrollFocus(this);
            }else if(Core.scene.getScrollFocus() == this){
                Core.scene.setScrollFocus(null);
            }
        });
    }
}
