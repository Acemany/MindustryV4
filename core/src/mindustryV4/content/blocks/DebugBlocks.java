package mindustryV4.content.blocks;

import com.badlogic.gdx.utils.Array;
import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import mindustryV4.content.Liquids;
import mindustryV4.entities.Player;
import mindustryV4.entities.TileEntity;
import mindustryV4.gen.Call;
import mindustryV4.game.ContentList;
import mindustryV4.type.Item;
import mindustryV4.type.Liquid;
import mindustryV4.world.BarType;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.PowerBlock;
import mindustryV4.world.blocks.distribution.Sorter;
import mindustryV4.world.blocks.power.PowerNode;
import mindustryV4.world.meta.BlockStat;
import ucore.graphics.Draw;
import ucore.scene.ui.ButtonGroup;
import ucore.scene.ui.ImageButton;
import ucore.scene.ui.layout.Table;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import static mindustryV4.Vars.*;

public class DebugBlocks extends BlockList implements ContentList{
    public static Block powerVoid, powerInfinite, itemSource, liquidSource, itemVoid;

    @Remote(targets = Loc.both, called = Loc.both, forward = true)
    public static void setLiquidSourceLiquid(Player player, Tile tile, Liquid liquid){
        LiquidSourceEntity entity = tile.entity();
        entity.source = liquid;
    }

    @Override
    public void load(){
        powerVoid = new PowerBlock("powervoid"){
            {
                powerCapacity = Float.MAX_VALUE;
                shadow = "shadow-round-1";
            }

            @Override
            public void setBars(){
                super.setBars();
                bars.remove(BarType.power);
            }

            @Override
            public void init(){
                super.init();
                stats.remove(BlockStat.powerCapacity);
            }
        };

        powerInfinite = new PowerNode("powerinfinite"){
            {
                powerCapacity = 10000f;
                maxNodes = 100;
                outputsPower = true;
                consumesPower = false;
                shadow = "shadow-round-1";
            }

            @Override
            public void update(Tile tile){
                super.update(tile);
                tile.entity.power.amount = powerCapacity;
            }
        };

        itemSource = new Sorter("itemsource"){
            {
                hasItems = true;
            }

            @Override
            public boolean outputsItems(){
                return true;
            }

            @Override
            public void setBars(){
                super.setBars();
                bars.remove(BarType.inventory);
            }

            @Override
            public void update(Tile tile){
                SorterEntity entity = tile.entity();
                entity.items.set(entity.sortItem, 1);
                tryDump(tile, entity.sortItem);
            }

            @Override
            public boolean acceptItem(Item item, Tile tile, Tile source){
                return false;
            }
        };

        liquidSource = new Block("liquidsource"){
            {
                update = true;
                solid = true;
                hasLiquids = true;
                liquidCapacity = 100f;
                configurable = true;
                outputsLiquid = true;
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
                    if(!control.unlocks.isUnlocked(items.get(i))) continue;

                    final int f = i;
                    ImageButton button = cont.addImageButton("liquid-icon-" + items.get(i).name, "clear-toggle", 24,
                            () -> Call.setLiquidSourceLiquid(null, tile, items.get(f))).size(38).group(group).get();
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
        };

        itemVoid = new Block("itemvoid"){
            {
                update = solid = true;
            }

            @Override
            public void handleItem(Item item, Tile tile, Tile source){
            }

            @Override
            public boolean acceptItem(Item item, Tile tile, Tile source){
                return true;
            }
        };
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
