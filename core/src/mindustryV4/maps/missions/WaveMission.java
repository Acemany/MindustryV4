package mindustryV4.maps.missions;

import io.anuke.arc.Core;
import io.anuke.arc.collection.Array;
import mindustryV4.game.*;
import mindustryV4.maps.Sector;
import mindustryV4.maps.generators.Generation;

import static mindustryV4.Vars.*;

public class WaveMission extends MissionWithStartingCore{
    private final int target;

    /**
     * Creates a wave survival mission with the player core being in the center of the map.
     * @param target The number of waves to be survived.
     */
    public WaveMission(int target){
        super();
        this.target = target;
    }

    /**
     * Creates a wave survival with the player core being at a custom location.
     * @param target The number of waves to be survived.
     * @param xCorePos The X coordinate of the custom core position.
     * @param yCorePos The Y coordinate of the custom core position.
     */
    public WaveMission(int target, int xCorePos, int yCorePos){
        super(xCorePos, yCorePos);
        this.target = target;
    }

    @Override
    public Array<SpawnGroup> getWaves(Sector sector){
        return state.rules.spawns;
    }

    @Override
    public void generate(Generation gen){
        generateCoreAtFirstSpawnPoint(gen, Team.blue);
    }

    @Override
    public void onBegin(){
        super.onBegin();

        world.pathfinder.activateTeamPath(waveTeam);
    }

    @Override
    public String displayString(){
        return state.wave > target ?
        Core.bundle.format(
                state.enemies() > 1 ?
                "mission.wave.enemies" :
                "mission.wave.enemy", target, target, state.enemies()) :
            Core.bundle.format("mission.wave", state.wave, target, (int)(state.wavetime/60));
    }

    @Override
    public String menuDisplayString(){
        return Core.bundle.format("mission.wave.menu", target);
    }

    @Override
    public void update(){

    }

    @Override
    public boolean isComplete(){
        return state.wave > target && state.enemies() == 0;
    }
}
