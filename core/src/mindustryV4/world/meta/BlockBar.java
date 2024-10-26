package mindustryV4.world.meta;

import mindustryV4.world.BarType;
import mindustryV4.world.Tile;

public class BlockBar{
    public final ValueSupplier value;
    public final BarType type;
    public final boolean top;

    public BlockBar(BarType type, boolean top, ValueSupplier value){
        this.value = value;
        this.type = type;
        this.top = top;
    }

    public interface ValueSupplier{
        float get(Tile tile);
    }
}
