package mindustryV4.ui.dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Scaling;
import mindustryV4.Vars;
import mindustryV4.core.Platform;
import mindustryV4.io.MapIO;
import mindustryV4.maps.Map;
import mindustryV4.maps.MapMeta;
import mindustryV4.maps.MapTileData;
import mindustryV4.ui.BorderImage;
import ucore.scene.event.Touchable;
import ucore.scene.ui.Image;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.TextButton;
import ucore.scene.ui.layout.Table;
import ucore.scene.utils.UIUtils;
import ucore.util.Bundles;
import ucore.util.Log;
import ucore.util.Strings;

import java.io.DataInputStream;

import static mindustryV4.Vars.*;

public class MapsDialog extends FloatingDialog{
    private FloatingDialog dialog;

    public MapsDialog(){
        super("$maps");

        addCloseButton();
        buttons().addImageTextButton("$editor.importmap", "icon-add", 14 * 2, () -> {
            Platform.instance.showFileChooser("$editor.importmap", "Map File", file -> {
                try{
                    DataInputStream stream = new DataInputStream(file.read());
                    MapMeta meta = MapIO.readMapMeta(stream);
                    MapTileData data = MapIO.readTileData(stream, meta, true);
                    stream.close();

                    String name = meta.tags.get("name", file.nameWithoutExtension());

                    if(world.maps.getByName(name) != null && !world.maps.getByName(name).custom){
                        ui.showError(Bundles.format("editor.import.exists", name));
                    }else if(world.maps.getByName(name) != null){
                        ui.showConfirm("$confirm", "$editor.overwrite.confirm", () -> {
                            world.maps.saveMap(name, data, meta.tags);
                            setup();
                        });
                    }else{
                        world.maps.saveMap(name, data, meta.tags);
                        setup();
                    }

                }catch(Exception e){
                    ui.showError(Bundles.format("editor.errorimageload", Strings.parseException(e, false)));
                    Log.err(e);
                }
            }, true, mapExtension);
        }).size(230f, 64f);

        shown(this::setup);
        onResize(() -> {
            if(dialog != null){
                dialog.hide();
            }
        });
    }

    void setup(){
        content().clear();

        Table maps = new Table();
        maps.marginRight(24);

        ScrollPane pane = new ScrollPane(maps);
        pane.setFadeScrollBars(false);

        int maxwidth = 4;
        float mapsize = 200f;

        int i = 0;
        for(Map map : world.maps.all()){

            if(i % maxwidth == 0){
                maps.row();
            }

            TextButton button = maps.addButton("", "clear", () -> showMapInfo(map)).width(mapsize).pad(8).get();
            button.clearChildren();
            button.margin(9);
            button.add(map.meta.tags.get("name", map.name)).growX().center().get().setEllipsis(true);
            button.row();
            button.addImage("white").growX().pad(4).color(Color.GRAY);
            button.row();
            button.stack(new Image(map.texture).setScaling(Scaling.fit), new BorderImage(map.texture).setScaling(Scaling.fit)).size(mapsize - 20f);
            button.row();
            button.add(map.custom ? "$custom" : "$builtin").color(Color.GRAY).padTop(3);

            i++;
        }

        if(world.maps.all().size == 0){
            maps.add("$maps.none");
        }

        content().add(pane).uniformX();
    }

    void showMapInfo(Map map){
        dialog = new FloatingDialog("$editor.mapinfo");
        dialog.addCloseButton();

        float mapsize = UIUtils.portrait() ? 160f : 300f;
        Table table = dialog.content();

        table.stack(new Image(map.texture).setScaling(Scaling.fit), new BorderImage(map.texture).setScaling(Scaling.fit)).size(mapsize);

        table.table("flat", desc -> {
            desc.top();
            Table t = new Table();
            t.margin(6);

            ScrollPane pane = new ScrollPane(t);
            desc.add(pane).grow();

            t.top();
            t.defaults().padTop(10).left();

            t.add("$editor.name").padRight(10).color(Color.GRAY).padTop(0);
            t.row();
            t.add(map.meta.tags.get("name", map.name)).growX().wrap().padTop(2);
            t.row();
            t.add("$editor.author").padRight(10).color(Color.GRAY);
            t.row();
            t.add(map.meta.author()).growX().wrap().padTop(2);
            t.row();
            t.add("$editor.description").padRight(10).color(Color.GRAY).top();
            t.row();
            t.add(map.meta.description()).growX().wrap().padTop(2);
            t.row();
            t.add("$editor.oregen.info").padRight(10).color(Color.GRAY);
            t.row();
            t.add(map.meta.hasOreGen() ? "$on" : "$off").padTop(2);
        }).height(mapsize).width(mapsize);

        table.row();

        table.addImageTextButton("$editor.openin", "icon-load-map", 16 * 2, () -> {
            try{
                Vars.ui.editor.beginEditMap(map.stream.get());
                dialog.hide();
                hide();
            }catch(Exception e){
                e.printStackTrace();
                ui.showError("$error.mapnotfound");
            }
        }).fillX().height(54f).marginLeft(10);

        table.addImageTextButton("$delete", "icon-trash-16", 16 * 2, () -> {
            ui.showConfirm("$confirm", Bundles.format("map.delete", map.name), () -> {
                world.maps.removeMap(map);
                dialog.hide();
                setup();
            });
        }).fillX().height(54f).marginLeft(10).disabled(!map.custom).touchable(map.custom ? Touchable.enabled : Touchable.disabled);

        dialog.show();
    }
}
