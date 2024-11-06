package mindustryV4.maps;

import io.anuke.arc.graphics.Texture;
import io.anuke.arc.collection.Array;
import io.anuke.annotations.Annotations.Serialize;
import io.anuke.arc.graphics.g2d.*;
import mindustryV4.game.Saves.SaveSlot;
import mindustryV4.game.SpawnGroup;
import mindustryV4.maps.missions.*;
import mindustryV4.type.ItemStack;
import io.anuke.arc.util.Pack;

import static mindustryV4.Vars.*;

@Serialize
public class Sector{
    private static final Mission victoryMission = new VictoryMission();

    /**Position on the map, can be positive or negative.*/
    public short x, y;
    /**Whether this sector has already been completed.*/
    public boolean complete;
    /**Slot ID of this sector's save. -1 means no save has been created.*/
    public int saveID = -1;
    /**Num of missions in this sector that have been completed so far.*/
    public int completedMissions;
    /**Display texture. Needs to be disposed.*/
    public transient TextureRegion texture;
    /**Missions of this sector-- what needs to be accomplished to unlock it.*/
    public transient Array<Mission> missions = new Array<>();
    /**Enemies spawned at this sector.*/
    public transient Array<SpawnGroup> spawns;
    /**Difficulty of the sector, measured by calculating distance from origin and applying scaling.*/
    public transient int difficulty;
    /**Items the player starts with on this sector.*/
    public transient Array<ItemStack> startingItems;

    public Mission getDominantMission(){
        for(Mission mission : missions){
            if(mission instanceof WaveMission || mission instanceof BattleMission){
                return mission;
            }
        }

        for(Mission mission : missions){
            if(mission instanceof BlockMission){
                return mission;
            }
        }
        return missions.first();
    }

    public Mission currentMission(){
        return completedMissions >= missions.size ? victoryMission : missions.get(completedMissions);
    }

    public int getSeed(){
        return Pack.shortInt(x, y);
    }

    public SaveSlot getSave(){
        return !hasSave() ? null : control.saves.getByID(saveID);
    }

    public boolean hasSave(){
        return !headless && control.saves.getByID(saveID) != null;
    }

    public int packedPosition(){
        return Pack.shortInt(x, y);
    }

    public String toString(){
        return "Sector{" +
        "pos=(" + x + ", " + y + ')' +
        ", complete=" + complete +
        ", difficulty=" + difficulty +
        '}';
    }
}
