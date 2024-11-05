package mindustryV4.maps.missions;

import io.anuke.arc.math.geom.Point2;
import io.anuke.arc.collection.Array;
import mindustryV4.Vars;
import mindustryV4.game.Team;
import mindustryV4.maps.generators.FortressGenerator;
import mindustryV4.maps.generators.Generation;
import mindustryV4.world.Tile;
import io.anuke.arc.Core;

import static mindustryV4.Vars.*;

public class BattleMission extends MissionWithStartingCore{
    final int spacing = 30;
    public static final int defaultXCorePos = 50;
    public static final int defaultYCorePos = 50;

    /** Creates a battle mission with the player core being at (@defaultXCorePos, @defaultYCorePos) */
    public BattleMission(){
        this(defaultXCorePos, defaultYCorePos);
    }

    /**
     * Creates a wave survival with the player core being at a custom location.
     * @param xCorePos The X coordinate of the custom core position.
     * @param yCorePos The Y coordinate of the custom core position.
     */
    public BattleMission(int xCorePos, int yCorePos){
        super(xCorePos, yCorePos);
    }

    @Override
    public String getIcon(){
        return "icon-mission-battle";
    }

    @Override
    public String displayString(){
        return Core.bundle.get("mission.battle");
    }

    @Override
    public Array<Point2> getSpawnPoints(Generation gen){
        return Array.with(new Point2(50, 50), new Point2(gen.width - 1 - spacing, gen.height - 1 - spacing));
    }

    @Override
    public void generate(Generation gen){
        generateCoreAtFirstSpawnPoint(gen, defaultTeam);

        if(state.teams.get(defaultTeam).cores.size == 0){
            return;
        }

        Tile core = state.teams.get(defaultTeam).cores.first();
        int enx = gen.width - 1 - spacing;
        int eny = gen.height - 1 - spacing;
        new FortressGenerator().generate(gen, Team.red, core.x, core.y, enx, eny);
    }

    @Override
    public boolean isComplete(){
        for(Team team : Vars.state.teams.enemiesOf(Vars.defaultTeam)){
            if(Vars.state.teams.isActive(team)){
                return false;
            }
        }
        return true;
    }
}
