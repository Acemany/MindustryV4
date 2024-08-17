package io.acemany.mindustryV4.world.blocks.storage;

import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.Tile;

public abstract class Unloader extends Block{
    protected final int timerUnload = timers++;

    public Unloader(String name){
        super(name);
        update = true;
        solid = true;
        health = 70;
        hasItems = true;
    }

    @Override
    public boolean canDump(Tile tile, Tile to, Item item){
        Block block = to.target().block();
        return !(block instanceof StorageBlock);
    }

    @Override
    public void setBars(){}
}
