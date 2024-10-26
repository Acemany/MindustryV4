package mindustryV4.entities.units;

import ucore.util.Bundles;

public enum UnitCommand{
    attack, retreat, patrol;

    private final String localized;

    UnitCommand(){
        localized = Bundles.get("command." + name());
    }

    public String localized(){
        return localized;
    }
}
