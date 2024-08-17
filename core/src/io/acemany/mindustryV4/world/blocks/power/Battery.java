package io.acemany.mindustryV4.world.blocks.power;

public class Battery extends PowerDistributor{

    public Battery(String name){
        super(name);
        outputsPower = true;
        consumesPower = true;
    }
}
