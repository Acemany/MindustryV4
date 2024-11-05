package mindustryV4.ui.dialogs;

import io.anuke.arc.Core;
import io.anuke.arc.graphics.Color;
import mindustryV4.graphics.Pal;
import io.anuke.arc.scene.ui.Dialog;

import static mindustryV4.Vars.telegramURL;
import static mindustryV4.Vars.ui;

public class TelegramDialog extends Dialog{

    public TelegramDialog(){
        super("", "dialog");

        float h = 70f;

        cont.margin(12f);

        Color color = Color.valueOf("7289da");

        cont.table(t -> {
            t.background("button").margin(0);

            t.table(img -> {
                img.addImage("white").height(h - 5).width(40f).color(color);
                img.row();
                img.addImage("white").height(5).width(40f).color(color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
            }).expandY();

            t.table(i -> {
                i.background("button");
                i.addImage("icon-telegram").size(14 * 3);
            }).size(h).left();

            t.add("$telegram").color(Pal.accent).growX().padLeft(10f);
        }).size(520f, h).pad(10f);

        buttons.defaults().size(170f, 50);

        buttons.addButton("$back", this::hide);
        buttons.addButton("$copylink", () -> {
            Core.app.getClipboard().setContents(telegramURL);
        });
        buttons.addButton("$openlink", () -> {
            if(!Core.net.openURI(telegramURL)){
                ui.showError("$linkfail");
                Core.app.getClipboard().setContents(telegramURL);
            }
        });
    }
}