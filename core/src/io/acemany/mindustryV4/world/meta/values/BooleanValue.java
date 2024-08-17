package io.acemany.mindustryV4.world.meta.values;

import io.acemany.mindustryV4.world.meta.StatValue;
import io.anuke.ucore.scene.ui.layout.Table;

public class BooleanValue implements StatValue{
    private final boolean value;

    public BooleanValue(boolean value){
        this.value = value;
    }

    @Override
    public void display(Table table){
        table.add(!value ? "$text.no" : "$text.yes");
    }
}
