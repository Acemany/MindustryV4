package mindustryV4.world.blocks.production;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mindustryV4.world.Tile;
import ucore.graphics.Draw;

public class Pulverizer extends GenericCrafter{
    protected TextureRegion rotatorRegion;

    public Pulverizer(String name){
        super(name);
        hasItems = true;
    }

    @Override
    public void load(){
        super.load();

        rotatorRegion = Draw.region(name + "-rotator");
    }

    @Override
    public void draw(Tile tile){
        GenericCrafterEntity entity = tile.entity();

        Draw.rect(region, tile.drawx(), tile.drawy());
        Draw.rect(rotatorRegion, tile.drawx(), tile.drawy(), entity.totalProgress * 2f);
    }

    @Override
    public TextureRegion[] getIcon(){
        return new TextureRegion[]{Draw.region(name), Draw.region(name + "-rotator")};
    }
}
