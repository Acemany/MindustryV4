package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import mindustryV4.graphics.Palette;
import ucore.scene.ui.Dialog;

import static mindustryV4.Vars.telegramURL;
import static mindustryV4.Vars.ui;

public class TelegramDialog extends Dialog{

    public TelegramDialog(){
        super("", "dialog");

        float h = 70f;

        content().margin(12f);

        Color color = Color.valueOf("7289da");

        content().table(t -> {
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

            t.add("$text.telegram").color(Palette.accent).growX().padLeft(10f);
        }).size(520f, h).pad(10f);

        buttons().defaults().size(170f, 50);

        buttons().addButton("$text.back", this::hide);
        buttons().addButton("$text.copylink", () -> {
            Gdx.app.getClipboard().setContents(telegramURL);
        });
        buttons().addButton("$text.openlink", () -> {
            if(!Gdx.net.openURI(telegramURL)){
                ui.showError("$text.linkfail");
                Gdx.app.getClipboard().setContents(telegramURL);
            }
        });
    }
}