package io.acemany.mindustryV4.world.blocks.storage;

public class Vault extends StorageBlock{

    public Vault(String name){
        super(name);
        solid = true;
        update = false;
        destructible = true;
        itemCapacity = 1000;
    }

}
