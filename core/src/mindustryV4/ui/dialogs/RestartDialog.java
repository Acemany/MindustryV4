package mindustryV4.ui.dialogs;

import mindustryV4.core.GameState.State;
import mindustryV4.game.Team;
import mindustryV4.maps.Sector;
import ucore.util.Bundles;

import static mindustryV4.Vars.*;

public class RestartDialog extends FloatingDialog{
    private Team winner;

    public RestartDialog(){
        super("$gameover");
        setFillParent(false);
        shown(this::rebuild);
    }

    public void show(Team winner){
        this.winner = winner;
        show();
    }

    void rebuild(){
        buttons().clear();
        content().clear();

        buttons().margin(10);

        if(state.mode.isPvp){
            content().add(Bundles.format("gameover.pvp",winner.localized())).pad(6);
            buttons().addButton("$menu", () -> {
                hide();
                state.set(State.menu);
                logic.reset();
            }).size(130f, 60f);
        }else if(world.getSector() == null){
            if(control.isHighScore()){
                content().add("$highscore").pad(6);
                content().row();
            }
            content().add(Bundles.format("wave.lasted", state.wave)).pad(12);

            buttons().addButton("$menu", () -> {
                hide();
                state.set(State.menu);
                logic.reset();
            }).size(130f, 60f);
        }else{
            content().add("$sector.gameover");
            buttons().addButton("$menu", () -> {
                hide();
                state.set(State.menu);
                logic.reset();
            }).size(130f, 60f);

            buttons().addButton("$sector.retry", () -> {
                Sector sector = world.getSector();
                ui.loadLogic(() -> world.sectors.playSector(sector));
                hide();
            }).size(130f, 60f);
        }
    }
}
