package mindustryV4.entities.units.types;

import com.badlogic.gdx.utils.Queue;
import mindustryV4.content.blocks.Blocks;
import mindustryV4.entities.Player;
import mindustryV4.entities.TileEntity;
import mindustryV4.entities.Units;
import mindustryV4.entities.traits.BuilderTrait;
import mindustryV4.entities.units.BaseUnit;
import mindustryV4.entities.units.FlyingUnit;
import mindustryV4.entities.units.UnitCommand;
import mindustryV4.entities.units.UnitState;
import mindustryV4.game.EventType.BuildSelectEvent;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Palette;
import mindustryV4.type.Item;
import mindustryV4.type.ItemStack;
import mindustryV4.type.ItemType;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.BuildBlock;
import mindustryV4.world.blocks.BuildBlock.BuildEntity;
import mindustryV4.world.meta.BlockFlag;
import ucore.core.Events;
import ucore.entities.EntityGroup;
import ucore.util.Geometry;
import ucore.util.Mathf;
import ucore.util.Structs;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static mindustryV4.Vars.unitGroups;
import static mindustryV4.Vars.world;

public class Drone extends FlyingUnit implements BuilderTrait{
    protected static int timerRepairEffect = timerIndex++;

    protected Item targetItem;
    protected Tile mineTile;
    protected Queue<BuildRequest> placeQueue = new Queue<>();
    protected boolean isBreaking;

    public final UnitState

    build = new UnitState(){

        public void entered(){
            if(!(target instanceof BuildEntity)){
                target = null;
            }
        }

        public void update(){
            BuildEntity entity = (BuildEntity) target;
            TileEntity core = getClosestCore();

            if(entity == null){
                setState(repair);
                return;
            }

            if(core == null) return;

            if((entity.progress() < 1f || entity.progress() > 0f) && entity.tile.block() instanceof BuildBlock){ //building is valid
                if(!isBuilding() && distanceTo(target) < placeDistance * 0.9f){ //within distance, begin placing
                    if(isBreaking){
                        getPlaceQueue().addLast(new BuildRequest(entity.tile.x, entity.tile.y));
                    }else{
                        getPlaceQueue().addLast(new BuildRequest(entity.tile.x, entity.tile.y, entity.tile.getRotation(), entity.recipe));
                    }
                }

                //if it's missing requirements, try and mine them
                if(entity.recipe != null){
                    for(ItemStack stack : entity.recipe.requirements){
                        if(!core.items.has(stack.item, stack.amount) && type.toMine.contains(stack.item)){
                            targetItem = stack.item;
                            getPlaceQueue().clear();
                            setState(mine);
                            return;
                        }
                    }
                }

                circle(placeDistance * 0.7f);
            }else{ //building isn't valid
                setState(repair);
            }
        }
    },

    repair = new UnitState(){

        public void entered(){
            target = null;
        }

        public void update(){

            retarget(() -> {
                target = Units.findDamagedTile(team, x, y);

                if(target == null){
                    setState(mine);
                }
            });

            if(target == null) return;

            if(target.distanceTo(Drone.this) > type.range){
                circle(type.range*0.9f);
            }else{
                getWeapon().update(Drone.this, target.getX(), target.getY());
            }
        }
    },

    mine = new UnitState(){
        public void entered(){
            target = null;
        }

        public void update(){
            TileEntity entity = getClosestCore();

            if(entity == null) return;

            if(targetItem == null){
                findItem();
            }

            //core full
            if(targetItem != null && entity.tile.block().acceptStack(targetItem, 1, entity.tile, Drone.this) == 0){
                setState(repair);
                return;
            }

            //if inventory is full, drop it off.
            if(inventory.isFull()){
                setState(drop);
            }else{
                if(targetItem != null && !inventory.canAcceptItem(targetItem)){
                    setState(drop);
                    return;
                }

                retarget(() -> {
                    if(getMineTile() == null){
                        findItem();
                    }

                    if(targetItem == null) return;

                    target = world.indexer.findClosestOre(x, y, targetItem);
                });

                if(target instanceof Tile){
                    moveTo(type.range / 1.5f);

                    if(distanceTo(target) < type.range && mineTile != target){
                        setMineTile((Tile) target);
                    }

                    if(((Tile) target).block() != Blocks.air){
                        setState(drop);
                    }
                }
            }
        }

        public void exited(){
            setMineTile(null);
        }
    },
    drop = new UnitState(){
        public void entered(){
            target = null;
        }

        public void update(){
            if(inventory.isEmpty()){
                setState(mine);
                return;
            }

            if(inventory.getItem().item.type != ItemType.material){
                inventory.clearItem();
                setState(mine);
                return;
            }

            target = getClosestCore();

            if(target == null) return;

            TileEntity tile = (TileEntity) target;

            if(distanceTo(target) < type.range){
                if(tile.tile.block().acceptStack(inventory.getItem().item, inventory.getItem().amount, tile.tile, Drone.this) == inventory.getItem().amount){
                    Call.transferItemTo(inventory.getItem().item, inventory.getItem().amount, x, y, tile.tile);
                    inventory.clearItem();
                }

                setState(repair);
            }

            circle(type.range / 1.8f);
        }
    },
    retreat = new UnitState(){
        public void entered(){
            target = null;
        }

        public void update(){
            if(health >= maxHealth()){
                state.set(attack);
            }else if(!targetHasFlag(BlockFlag.repair)){
                if(timer.get(timerTarget, 20)){
                    Tile target = Geometry.findClosest(x, y, world.indexer.getAllied(team, BlockFlag.repair));
                    if(target != null) Drone.this.target = target.entity;
                }
            }else{
                circle(40f);
            }
        }
    };

    static{

        Events.on(BuildSelectEvent.class, event -> {
            EntityGroup<BaseUnit> group = unitGroups[event.team.ordinal()];

            if(!(event.builder instanceof Player) || !(event.tile.entity instanceof BuildEntity)) return;
            BuildEntity entity = event.tile.entity();

            for(BaseUnit unit : group.all()){
                if(unit instanceof Drone){
                    Drone drone = (Drone)unit;
                    if(drone.isBuilding()){
                        //stop building if opposite building begins.
                        BuildRequest req = drone.getCurrentRequest();
                        if(req.breaking != event.breaking && req.x == event.tile.x && req.y == event.tile.y){
                            drone.clearBuilding();
                            drone.setState(drone.repair);
                        }
                    }

                    drone.notifyPlaced(entity, event.breaking);
                }
            }
        });
    }

    private void notifyPlaced(BuildEntity entity, boolean isBreaking){
        float dist = Math.min(entity.distanceTo(x, y) - placeDistance, 0);

        if(!state.is(build) && dist / type.maxVelocity < entity.buildCost * 0.9f){
            target = entity;
            this.isBreaking = isBreaking;
            setState(build);
        }
    }

    @Override
    public void onCommand(UnitCommand command){
        //no
    }

    @Override
    public boolean canMine(Item item){
        return type.toMine.contains(item);
    }

    @Override
    public float getBuildPower(Tile tile){
        return type.buildPower;
    }

    @Override
    public float getMinePower(){
        return type.minePower;
    }

    @Override
    public Queue<BuildRequest> getPlaceQueue(){
        return placeQueue;
    }

    @Override
    public Tile getMineTile(){
        return mineTile;
    }

    @Override
    public void setMineTile(Tile tile){
        mineTile = tile;
    }

    @Override
    public void update(){
        super.update();

        if(state.is(repair) && target != null && target.getTeam() != team){
            target = null;
        }

        updateBuilding(this);
    }

    @Override
    protected void updateRotation(){
        if(target != null && ((state.is(repair) && target.distanceTo(this) < type.range) || state.is(mine))){
            rotation = Mathf.slerpDelta(rotation, angleTo(target), 0.3f);
        }else{
            rotation = Mathf.slerpDelta(rotation, velocity.angle(), 0.3f);
        }
    }

    @Override
    public void behavior(){
        if(health <= health * type.retreatPercent &&
                Geometry.findClosest(x, y, world.indexer.getAllied(team, BlockFlag.repair)) != null){
            setState(retreat);
        }
    }

    @Override
    public UnitState getStartState(){
        return repair;
    }

    @Override
    public void drawOver(){
        trail.draw(Palette.lightTrail, 3f);
        drawBuilding(this);
    }

    @Override
    public float drawSize(){
        return isBuilding() ? placeDistance * 2f : 30f;
    }

    protected void findItem(){
        TileEntity entity = getClosestCore();
        if(entity == null){
            return;
        }
        targetItem = Structs.findMin(type.toMine, (a, b) -> -Integer.compare(entity.items.get(a), entity.items.get(b)));
    }

    @Override
    public boolean canCreateBlocks(){
        return false;
    }

    @Override
    public void write(DataOutput data) throws IOException{
        super.write(data);
        data.writeInt(mineTile == null || !state.is(mine) ? -1 : mineTile.packedPosition());
        data.writeInt(state.is(repair) && target instanceof TileEntity ? ((TileEntity)target).tile.packedPosition() : -1);
        writeBuilding(data);
    }

    @Override
    public void read(DataInput data, long time) throws IOException{
        super.read(data, time);
        int mined = data.readInt();
        int repairing = data.readInt();

        readBuilding(data);

        if(mined != -1){
            mineTile = world.tile(mined);
        }

        if(repairing != -1){
            Tile tile = world.tile(repairing);
            target = tile.entity;
            state.set(repair);
        }else{
            state.set(retreat);
        }
    }

}
