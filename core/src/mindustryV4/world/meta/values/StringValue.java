package mindustryV4.world.meta.values;

import mindustryV4.world.meta.StatValue;
import ucore.scene.ui.layout.Table;
import ucore.util.Strings;

public class StringValue implements StatValue{
    private final String value;

    public StringValue(String value, Object... args){
        this.value = Strings.formatArgs(value, args);
    }

    @Override
    public void display(Table table){
        table.add(value);
    }
}
