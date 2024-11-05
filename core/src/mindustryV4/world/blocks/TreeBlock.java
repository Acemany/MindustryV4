package mindustryV4.world.blocks;

import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.math.Mathf;
import mindustryV4.graphics.Layer;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;

public class TreeBlock extends Block{
    static final float shadowOffset = 10f;

    public TreeBlock(String name){
        super(name);
        solid = true;
        layer = Layer.power;
        expanded = true;
    }

    @Override
    public void draw(Tile tile){}

    @Override
    public void drawShadow(Tile tile){
        Draw.rect(region, tile.drawx(), tile.drawy(), Mathf.randomSeed(tile.pos(), 0, 4) * 90);
        Draw.rect(region, tile.drawx() - shadowOffset, tile.drawy() - shadowOffset, Mathf.randomSeed(tile.pos(), 0, 4) * 90);
    }

    @Override
    public void drawLayer(Tile tile){
        Draw.rect(region, tile.drawx(), tile.drawy(), Mathf.randomSeed(tile.pos(), 0, 4) * 90);
    }
}
