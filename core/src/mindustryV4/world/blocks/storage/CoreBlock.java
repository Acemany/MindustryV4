package mindustryV4.world.blocks.storage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import mindustryV4.Vars;
import mindustryV4.content.UnitTypes;
import mindustryV4.content.fx.Fx;
import mindustryV4.entities.Player;
import mindustryV4.entities.TileEntity;
import mindustryV4.entities.Unit;
import mindustryV4.entities.Units;
import mindustryV4.entities.traits.SpawnerTrait;
import mindustryV4.entities.units.BaseUnit;
import mindustryV4.entities.units.UnitType;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Palette;
import mindustryV4.graphics.Shaders;
import mindustryV4.maps.TutorialSector;
import mindustryV4.net.Net;
import mindustryV4.type.Item;
import mindustryV4.world.BarType;
import mindustryV4.world.Tile;
import mindustryV4.world.meta.BlockFlag;
import ucore.core.Effects;
import ucore.core.Graphics;
import ucore.core.Timers;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.util.EnumSet;
import ucore.util.Mathf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static mindustryV4.Vars.*;

public class CoreBlock extends StorageBlock{
    protected float droneRespawnDuration = 60 * 6;
    protected UnitType droneType = UnitTypes.spirit;

    protected TextureRegion openRegion;
    protected TextureRegion topRegion;

    public CoreBlock(String name){
        super(name);

        solid = false;
        solidifes = true;
        update = true;
        size = 3;
        hasItems = true;
        itemCapacity = 2000;
        viewRange = 200f;
        flags = EnumSet.of(BlockFlag.resupplyPoint, BlockFlag.target);
    }

    @Remote(called = Loc.server)
    public static void onUnitRespawn(Tile tile, Unit player){
        if(player == null || tile.entity == null) return;

        CoreEntity entity = tile.entity();
        Effects.effect(Fx.spawn, entity);
        entity.solid = false;
        entity.progress = 0;
        entity.currentUnit = player;
        entity.currentUnit.heal();
        entity.currentUnit.rotation = 90f;
        entity.currentUnit.setNet(tile.drawx(), tile.drawy());
        entity.currentUnit.add();
        entity.currentUnit = null;

        if(player instanceof Player){
            ((Player) player).endRespawning();
        }
    }

    @Remote(called = Loc.server)
    public static void setCoreSolid(Tile tile, boolean solid){
        if(tile == null) return;
        CoreEntity entity = tile.entity();
        if(entity != null) entity.solid = solid;
    }

    @Override
    public void onProximityUpdate(Tile tile) {
        state.teams.get(tile.getTeam()).cores.add(tile);
    }

    @Override
    public boolean canBreak(Tile tile){
        return state.teams.get(tile.getTeam()).cores.size > 1;
    }

    @Override
    public void removed(Tile tile){
        state.teams.get(tile.getTeam()).cores.remove(tile);
    }

    @Override
    public void placed(Tile tile){
        state.teams.get(tile.getTeam()).cores.add(tile);
    }

    @Override
    public void setBars(){
        super.setBars();

        bars.remove(BarType.inventory);
    }

    @Override
    public void load(){
        super.load();

        openRegion = Draw.region(name + "-open");
        topRegion = Draw.region(name + "-top");
    }

    @Override
    public void draw(Tile tile){
        CoreEntity entity = tile.entity();

        Draw.rect(entity.solid ? Draw.region(name) : openRegion, tile.drawx(), tile.drawy());

        Draw.alpha(entity.heat);
        Draw.rect(topRegion, tile.drawx(), tile.drawy());
        Draw.color();

        if(entity.currentUnit != null){
            Unit player = entity.currentUnit;

            TextureRegion region = player.getIconRegion();

            Shaders.build.region = region;
            Shaders.build.progress = entity.progress;
            Shaders.build.color.set(Palette.accent);
            Shaders.build.time = -entity.time / 10f;

            Graphics.shader(Shaders.build, false);
            Shaders.build.apply();
            Draw.rect(region, tile.drawx(), tile.drawy());
            Graphics.shader();

            Draw.color(Palette.accent);

            Lines.lineAngleCenter(
                    tile.drawx() + Mathf.sin(entity.time, 6f, Vars.tilesize / 3f * size),
                    tile.drawy(),
                    90,
                    size * Vars.tilesize / 2f);

            Draw.reset();
        }
    }

    @Override
    public boolean isSolidFor(Tile tile){
        CoreEntity entity = tile.entity();

        return entity.solid;
    }

    @Override
    public void handleItem(Item item, Tile tile, Tile source){
        if(Net.server() || !Net.active()) super.handleItem(item, tile, source);
    }

    @Override
    public void update(Tile tile){
        CoreEntity entity = tile.entity();

        if(!entity.solid && !Units.anyEntities(tile)){
            Call.setCoreSolid(tile, true);
        }

        if(entity.currentUnit != null){
            if(!entity.currentUnit.isDead()){
                entity.currentUnit = null;
                return;
            }
            entity.heat = Mathf.lerpDelta(entity.heat, 1f, 0.1f);
            entity.time += entity.delta();
            entity.progress += 1f / (entity.currentUnit instanceof Player ? state.mode.respawnTime : droneRespawnDuration) * entity.delta();

            if(entity.progress >= 1f){
                Call.onUnitRespawn(tile, entity.currentUnit);
            }
        }else if(!netServer.isWaitingForPlayers()){
            entity.warmup += Timers.delta();

            if(entity.solid && entity.warmup > 60f && unitGroups[tile.getTeamID()].getByID(entity.droneID) == null && !Net.client()){

                boolean found = false;
                for(BaseUnit unit : unitGroups[tile.getTeamID()].all()){
                    if(unit.getType().id == droneType.id){
                        entity.droneID = unit.id;
                        found = true;
                        break;
                    }
                }

                if(!found && !TutorialSector.supressDrone()){
                    BaseUnit unit = droneType.create(tile.getTeam());
                    unit.setSpawner(tile);
                    unit.setDead(true);
                    unit.add();

                    useContent(tile, droneType);

                    entity.droneID = unit.id;
                }
            }

            entity.heat = Mathf.lerpDelta(entity.heat, 0f, 0.1f);
        }
    }

    @Override
    public TileEntity newEntity(){
        return new CoreEntity();
    }

    public class CoreEntity extends StorageEntity implements SpawnerTrait{
        public Unit currentUnit;
        int droneID = -1;
        boolean solid = true;
        float warmup;
        float progress;
        float time;
        float heat;

        @Override
        public void updateSpawning(Unit unit){
            if(!netServer.isWaitingForPlayers() && currentUnit == null){
                currentUnit = unit;
                progress = 0f;
                unit.set(tile.drawx(), tile.drawy());
            }
        }

        @Override
        public float getSpawnProgress(){
            return progress;
        }

        @Override
        public void write(DataOutput stream) throws IOException{
            stream.writeBoolean(solid);
            stream.writeInt(droneID);
        }

        @Override
        public void read(DataInput stream) throws IOException{
            solid = stream.readBoolean();
            droneID = stream.readInt();
        }
    }
}
