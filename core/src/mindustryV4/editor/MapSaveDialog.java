package mindustryV4.editor;

import mindustryV4.core.Platform;
import mindustryV4.maps.Map;
import mindustryV4.ui.dialogs.FloatingDialog;
import ucore.function.Consumer;
import ucore.scene.ui.TextButton;
import ucore.scene.ui.TextField;

import static mindustryV4.Vars.ui;
import static mindustryV4.Vars.world;

public class MapSaveDialog extends FloatingDialog{
    private TextField field;
    private Consumer<String> listener;

    public MapSaveDialog(Consumer<String> cons){
        super("$editor.savemap");
        field = new TextField();
        listener = cons;

        Platform.instance.addDialog(field);

        shown(() -> {
            content().clear();
            content().label(() -> {
                Map map = world.maps.getByName(field.getText());
                if(map != null){
                    if(map.custom){
                        return "$editor.overwrite";
                    }else{
                        return "$editor.failoverwrite";
                    }
                }
                return "";
            }).colspan(2);
            content().row();
            content().add("$editor.mapname").padRight(14f);
            content().add(field).size(220f, 48f);
        });

        buttons().defaults().size(200f, 50f).pad(2f);
        buttons().addButton("$cancel", this::hide);

        TextButton button = new TextButton("$save");
        button.clicked(() -> {
            if(!invalid()){
                cons.accept(field.getText());
                hide();
            }
        });
        button.setDisabled(this::invalid);
        buttons().add(button);
    }

    public void save(){
        if(!invalid()){
            listener.accept(field.getText());
        }else{
            ui.showError("$editor.failoverwrite");
        }
    }

    public void setFieldText(String text){
        field.setText(text);
    }

    private boolean invalid(){
        if(field.getText().isEmpty()){
            return true;
        }
        Map map = world.maps.getByName(field.getText());
        return map != null && !map.custom;
    }
}
