package mindustryV4.ui;

import mindustryV4.type.Liquid;
import ucore.scene.ui.Image;
import ucore.scene.ui.layout.Table;

/**An ItemDisplay, but for liquids.*/
public class LiquidDisplay extends Table{

    public LiquidDisplay(Liquid liquid){
        add(new Image(liquid.getContentIcon())).size(8*3);
        add(liquid.localizedName()).padLeft(3);
    }
}
