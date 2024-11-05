package mindustryV4.world.blocks.defense.turrets;

import io.anuke.arc.collection.*;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.Vars;
import mindustryV4.entities.bullet.BulletType;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.entities.type.Unit;
import mindustryV4.graphics.Pal;
import mindustryV4.type.Item;
import mindustryV4.ui.Bar;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockStat;
import mindustryV4.world.meta.values.ItemFilterValue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ItemTurret extends CooledTurret{
    protected int maxAmmo = 30;
    protected ObjectMap<Item, BulletType> ammo = new ObjectMap<>();

    public ItemTurret(String name){
        super(name);
        hasItems = true;
    }

    public Item[] getAmmoTypes(){
        return ammo.keys().toArray().toArray();
    }

    /**Initializes accepted ammo map. Format: [item1, bullet1, item2, bullet2...]*/
    protected void ammo(Object... objects){
        ammo = ObjectMap.of(objects);
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.remove(BlockStat.itemCapacity);
        stats.add(BlockStat.inputItems, new ItemFilterValue(item -> ammo.containsKey(item)));
    }


    @Override
    public void displayBars(Tile tile, Table bars){
        super.displayBars(tile, bars);

        TurretEntity entity = tile.entity();

        bars.add(new Bar("blocks.ammo", Pal.ammo, () -> (float)entity.totalAmmo / maxAmmo)).growX();
        bars.row();
    }

    @Override
    public int acceptStack(Item item, int amount, Tile tile, Unit source){
        TurretEntity entity = tile.entity();

        BulletType type = ammo.get(item);

        if(type == null) return 0;

        return Math.min((int) ((maxAmmo - entity.totalAmmo) / ammo.get(item).ammoMultiplier), amount);
    }

    @Override
    public void handleStack(Item item, int amount, Tile tile, Unit source){
        for(int i = 0; i < amount; i++){
            handleItem(item, tile, null);
        }
    }

    //currently can't remove items from turrets.
    @Override
    public int removeStack(Tile tile, Item item, int amount){
        return 0;
    }

    @Override
    public void handleItem(Item item, Tile tile, Tile source){
        TurretEntity entity = tile.entity();
        if(entity == null) return;

        BulletType type = ammo.get(item);
        entity.totalAmmo += type.ammoMultiplier;

        //find ammo entry by type
        for(int i = 0; i < entity.ammo.size; i++){
            ItemEntry entry = (ItemEntry)entity.ammo.get(i);

            //if found, put it to the right
            if(entry.item == item){
                entry.amount += type.ammoMultiplier;
                entity.ammo.swap(i, entity.ammo.size - 1);
                return;
            }
        }

        //must not be found
        entity.ammo.add(new ItemEntry(item, (int) type.ammoMultiplier));
    }

    @Override
    public boolean acceptItem(Item item, Tile tile, Tile source){
        TurretEntity entity = tile.entity();

        return ammo != null && ammo.get(item) != null && entity.totalAmmo + ammo.get(item).ammoMultiplier <= maxAmmo;
    }

    @Override
    public TileEntity newEntity(){
        return new ItemTurretEntity();
    }

    public class ItemTurretEntity extends TurretEntity{
        @Override
        public void write(DataOutput stream) throws IOException{
            stream.writeByte(ammo.size);
            for(AmmoEntry entry : ammo){
                ItemEntry i = (ItemEntry)entry;
                stream.writeByte(i.item.id);
                stream.writeShort(i.amount);
            }
        }

        @Override
        public void read(DataInput stream) throws IOException{
            byte amount = stream.readByte();
            for(int i = 0; i < amount; i++){
                Item item = Vars.content.item(stream.readByte());
                short a = stream.readShort();
                totalAmmo += a;
                ammo.add(new ItemEntry(item, a));
            }
        }
    }

    class ItemEntry extends AmmoEntry{
        protected Item item;

        ItemEntry(Item item, int amount){
            this.item = item;
            this.amount = amount;
        }

        @Override
        public BulletType type(){
            return ammo.get(item);
        }
    }
}
