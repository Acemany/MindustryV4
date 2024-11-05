package mindustryV4.world.blocks.sandbox;

import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import io.anuke.arc.collection.Array;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.scene.style.TextureRegionDrawable;
import io.anuke.arc.scene.ui.ButtonGroup;
import io.anuke.arc.scene.ui.ImageButton;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.content.Liquids;
import mindustryV4.entities.type.Player;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.gen.Call;
import mindustryV4.type.Liquid;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static mindustryV4.Vars.content;

public class LiquidSource extends Block{

    public LiquidSource(String name){
        super(name);
        update = true;
        solid = true;
        hasLiquids = true;
        liquidCapacity = 100f;
        configurable = true;
        outputsLiquid = true;
    }

    @Override
    public void setBars(){
        super.setBars();

        bars.remove("liquid");
    }

    @Override
    public void update(Tile tile){
        LiquidSourceEntity entity = tile.entity();

        tile.entity.liquids.add(entity.source, liquidCapacity);
        tryDumpLiquid(tile, entity.source);
    }

    @Override
    public void draw(Tile tile){
        super.draw(tile);

        LiquidSourceEntity entity = tile.entity();

        Draw.color(entity.source.color);
        Draw.rect("blank", tile.worldx(), tile.worldy(), 4f, 4f);
        Draw.color();
    }

    @Override
    public void buildTable(Tile tile, Table table){
        LiquidSourceEntity entity = tile.entity();

        Array<Liquid> items = content.liquids();

        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        Table cont = new Table();

        for(int i = 0; i < items.size; i++){
            final int f = i;
            ImageButton button = cont.addImageButton("clear", "clear-toggle", 24,
                    () -> Call.setLiquidSourceLiquid(null, tile, items.get(f))).size(38).group(group).get();
            button.getStyle().imageUp = new TextureRegionDrawable(items.get(i).iconRegion);
            button.setChecked(entity.source.id == f);

            if(i % 4 == 3){
                cont.row();
            }
        }

        table.add(cont);
    }

    @Override
    public TileEntity newEntity(){
        return new LiquidSourceEntity();
    }

    @Remote(targets = Loc.both, called = Loc.both, forward = true)
    public static void setLiquidSourceLiquid(Player player, Tile tile, Liquid liquid){
        LiquidSourceEntity entity = tile.entity();
        entity.source = liquid;
    }

    class LiquidSourceEntity extends TileEntity{
        public Liquid source = Liquids.water;

        @Override
        public void writeConfig(DataOutput stream) throws IOException{
            stream.writeByte(source.id);
        }

        @Override
        public void readConfig(DataInput stream) throws IOException{
            source = content.liquid(stream.readByte());
        }
    }
}
