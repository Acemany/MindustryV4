package mindustryV4.maps.generators;

import mindustryV4.type.Loadout;
import mindustryV4.world.Tile;

public abstract class Generator{
    public int width, height;

    public Generator(int width, int height){
        this.width = width;
        this.height = height;
    }

    public Generator(){}

    public void init(Loadout loadout){

    }

    public abstract void generate(Tile[][] tiles);
}
