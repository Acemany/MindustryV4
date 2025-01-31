package mindustryV4.editor;

import com.badlogic.gdx.utils.ObjectMap;
import mindustryV4.core.Platform;
import mindustryV4.ui.dialogs.FloatingDialog;
import ucore.core.Settings;
import ucore.scene.ui.TextArea;
import ucore.scene.ui.TextField;

public class MapInfoDialog extends FloatingDialog{
    private final MapEditor editor;

    private TextArea description;
    private TextField author;
    private TextField name;

    public MapInfoDialog(MapEditor editor){
        super("$editor.mapinfo");
        this.editor = editor;

        addCloseButton();

        shown(this::setup);

        hidden(() -> {

        });
    }

    private void setup(){
        content().clear();

        ObjectMap<String, String> tags = editor.getTags();

        content().add("$editor.name").padRight(8).left();

        content().defaults().padTop(15);

        name = content().addField(tags.get("name", ""), text -> {
            tags.put("name", text);
        }).size(400, 55f).get();
        name.setMessageText("$unknown");

        content().row();

        content().add("$editor.description").padRight(8).left();

        description = content().addArea(tags.get("description", ""), "textarea", text -> {
            tags.put("description", text);
        }).size(400f, 140f).get();

        content().row();

        content().add("$editor.author").padRight(8).left();

        author = content().addField(tags.get("author", Settings.getString("mapAuthor", "")), text -> {
            tags.put("author", text);
            Settings.putString("mapAuthor", text);
            Settings.save();
        }).size(400, 55f).get();
        author.setMessageText("$unknown");

        content().row();

        content().add().padRight(8).left();
        content().addCheck("$editor.oregen", enabled -> {
            tags.put("oregen", enabled ? "1" : "0");
        }).update(c -> c.setChecked(!tags.get("oregen", "0").equals("0"))).left();

        name.change();
        description.change();
        author.change();

        Platform.instance.addDialog(name, 50);
        Platform.instance.addDialog(author, 50);
        Platform.instance.addDialog(description, 1000);
    }
}
