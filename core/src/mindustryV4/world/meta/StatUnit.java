package mindustryV4.world.meta;

import ucore.util.Bundles;

import java.util.Locale;

/**
 * Defines a unit of measurement for block stats.
 */
public enum StatUnit{
    blocks,
    powerSecond,
    liquidSecond,
    itemsSecond,
    pixelsSecond,
    liquidUnits,
    powerUnits,
    degrees,
    seconds,
    none,
    items;

    public String localized(){
        if(this == none) return "";
        return Bundles.get("unit." + name().toLowerCase(Locale.ROOT));
    }
}
