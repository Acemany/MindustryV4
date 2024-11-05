package mindustryV4.world.meta.values;

import mindustryV4.game.UnlockableContent;
import mindustryV4.type.Liquid;
import mindustryV4.ui.LiquidDisplay;
import mindustryV4.world.meta.ContentStatValue;
import io.anuke.arc.scene.ui.layout.Table;

public class LiquidValue implements ContentStatValue{
    private final Liquid liquid;

    public LiquidValue(Liquid liquid){
        this.liquid = liquid;
    }

    @Override
    public UnlockableContent[] getValueContent(){
        return new UnlockableContent[]{liquid};
    }

    @Override
    public void display(Table table){
        table.add(new LiquidDisplay(liquid));
    }
}
