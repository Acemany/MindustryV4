package mindustryV4.ui.dialogs;

import mindustryV4.Vars;
import mindustryV4.maps.Sector;
import ucore.util.Bundles;

public class MissionDialog extends FloatingDialog{

    public MissionDialog(){
        super("$mission.complete");
        setFillParent(false);
    }

    public void show(Sector sector){
        buttons().clear();
        content().clear();

        buttons().addButton("$nextmission", () -> {
            hide();
            Vars.ui.paused.runExitSave();
            Vars.ui.sectors.show();
        }).size(190f, 64f);

        buttons().addButton("$continue", this::hide).size(190f, 64f);

        content().add(Bundles.format("mission.complete.body", sector.x, sector.y)).pad(10);
        show();
    }
}
