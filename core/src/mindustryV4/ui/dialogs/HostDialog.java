package mindustryV4.ui.dialogs;

import com.badlogic.gdx.graphics.Color;
import mindustryV4.Vars;
import mindustryV4.entities.Player;
import mindustryV4.net.Net;
import ucore.core.Settings;
import ucore.core.Timers;
import ucore.scene.ui.ImageButton;
import ucore.util.Bundles;
import ucore.util.Strings;

import java.io.IOException;

import static mindustryV4.Vars.players;
import static mindustryV4.Vars.ui;

public class HostDialog extends FloatingDialog{
    float w = 300;

    public HostDialog(){
        super("$hostserver");

        Player player = players[0];

        addCloseButton();

        content().table(t -> {
            t.add("$name").padRight(10);
            t.addField(Settings.getString("name"), text -> {
                player.name = text;
                Settings.put("name", text);
                Settings.save();
                ui.listfrag.rebuild();
            }).grow().pad(8).get().setMaxLength(40);

            ImageButton button = t.addImageButton("white", "clear-full", 40, () -> {
                new ColorPickDialog().show(color -> {
                    player.color.set(color);
                    Settings.putInt("color-0", Color.rgba8888(color));
                    Settings.save();
                });
            }).size(54f).get();
            button.update(() -> button.getStyle().imageUpColor = player.color);
        }).width(w).height(70f).pad(4).colspan(3);

        content().row();

        content().add().width(65f);

        content().addButton("$host", () -> {
            if(Settings.getString("name").trim().isEmpty()){
                ui.showInfo("$noname");
                return;
            }

            ui.loadfrag.show("$hosting");
            Timers.runTask(5f, () -> {
                try{
                    Net.host(Vars.port);
                    player.isAdmin = true;
                }catch(IOException e){
                    ui.showError(Bundles.format("server.error", Strings.parseException(e, false)));
                }
                ui.loadfrag.hide();
                hide();
            });
        }).width(w).height(70f);

        content().addButton("?", () -> ui.showInfo("$host.info")).size(65f, 70f).padLeft(6f);
    }
}
