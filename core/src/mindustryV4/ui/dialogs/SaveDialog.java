package mindustryV4.ui.dialogs;

import io.anuke.arc.Core;
import io.anuke.arc.scene.ui.TextButton;
import io.anuke.arc.util.Time;
import mindustryV4.core.GameState.State;
import mindustryV4.game.Saves.SaveSlot;

import static mindustryV4.Vars.*;

public class SaveDialog extends LoadDialog{

    public SaveDialog(){
        super("$savegame");

        update(() -> {
            if(state.is(State.menu) && isShown()){
                hide();
            }
        });
    }

    public void addSetup(){
        slots.row();
        slots.addImageTextButton("$save.new", "icon-add",14 * 3, () ->
                ui.showTextInput("$save", "$save.newslot", "", text -> {
                    ui.loadAnd("$saving", () -> {
                        control.saves.addSave(text);
                        Core.app.post(() -> Core.app.post(this::setup));
                    });
                })
        ).fillX().margin(10f).minWidth(300f).height(70f).pad(4f).padRight(-4);
    }

    @Override
    public void modifyButton(TextButton button, SaveSlot slot){
        button.clicked(() -> {
            if(button.childrenPressed()) return;

            ui.showConfirm("$overwrite", "$save.overwrite", () -> save(slot));
        });
    }

    void save(SaveSlot slot){

        ui.loadfrag.show("$saveload");

        Time.runTask(5f, () -> {
            hide();
            ui.loadfrag.hide();
            try{
                slot.save();
            }catch(Throwable e){
                e.printStackTrace();

                ui.showError("[accent]" + Core.bundle.get("savefail"));
            }
        });
    }
}
