package io.acemany.mindustryV4.world.meta.values;

import io.acemany.mindustryV4.game.UnlockableContent;
import io.acemany.mindustryV4.type.Liquid;
import io.acemany.mindustryV4.ui.LiquidDisplay;
import io.acemany.mindustryV4.world.meta.ContentStatValue;
import io.anuke.ucore.scene.ui.layout.Table;

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
