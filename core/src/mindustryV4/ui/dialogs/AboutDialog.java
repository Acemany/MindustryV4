package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import mindustryV4.graphics.Palette;
import mindustryV4.io.Contributors;
import mindustryV4.io.Contributors.Contributor;
import mindustryV4.ui.Links;
import mindustryV4.ui.Links.LinkEntry;
import ucore.core.Core;
import ucore.core.Timers;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.layout.Cell;
import ucore.scene.ui.layout.Table;
import ucore.scene.utils.UIUtils;
import ucore.util.OS;
import ucore.util.Strings;

import static mindustryV4.Vars.ios;
import static mindustryV4.Vars.ui;

public class AboutDialog extends FloatingDialog{
    private Array<Contributor> contributors = new Array<>();
    private static ObjectSet<String> bannedItems = ObjectSet.with("dev-builds", "taskboard");

    public AboutDialog(){
        super("$about.button");

        Contributors.getContributors(out -> contributors = out, Throwable::printStackTrace);

        shown(this::setup);
        onResize(this::setup);
    }

    void setup(){
        content().clear();
        buttons().clear();

        float h = UIUtils.portrait() ? 90f : 80f;
        float w = UIUtils.portrait() ? 330f : 600f;

        Table in = new Table();
        ScrollPane pane = new ScrollPane(in);

        for(LinkEntry link : Links.getLinks()){
            if((ios || OS.isMac) && bannedItems.contains(link.name)){ //because Apple doesn't like me mentioning things
                continue;
            }

            Table table = new Table("underline-2");
            table.margin(0);
            table.table(img -> {
                img.addImage("white").height(h - 5).width(40f).color(link.color);
                img.row();
                img.addImage("white").height(5).width(40f).color(link.color.cpy().mul(0.8f, 0.8f, 0.8f, 1f));
            }).expandY();

            table.table(i -> {
                i.background("button-edge-3");
                i.addImage("icon-" + link.name).size(14 * 3f);
            }).size(h - 5, h);

            table.table(inset -> {
                inset.add("[accent]" + Strings.capitalize(link.name.replace("-", " "))).growX().left();
                inset.row();
                inset.labelWrap(link.description).width(w - 100f).color(Color.LIGHT_GRAY).growX();
            }).padLeft(8);

            table.addImageButton("icon-link", 14 * 3, () -> {
                if(!Gdx.net.openURI(link.link)){
                    ui.showError("$linkfail");
                    Gdx.app.getClipboard().setContents(link.link);
                }
            }).size(h - 5, h);

            in.add(table).size(w, h).padTop(5).row();
        }

        shown(() -> Timers.run(1f, () -> Core.scene.setScrollFocus(pane)));

        content().add(pane).growX();

        addCloseButton();

        buttons().addButton("$credits", this::showCredits).size(200f, 64f);

        if(!ios && !OS.isMac){
            buttons().addButton("$changelog.title", ui.changelog::show).size(200f, 64f);
        }

        if(UIUtils.portrait()){
            for(Cell<?> cell : buttons().getCells()){
                cell.width(140f);
            }
        }

    }

    public void showCredits(){
        FloatingDialog dialog = new FloatingDialog("$credits");
        dialog.addCloseButton();
        dialog.content().add("$credits.text");
        dialog.content().row();
        if(!contributors.isEmpty()){
            dialog.content().addImage("blank").color(Palette.accent).fillX().height(3f).pad(3f);
            dialog.content().row();
            dialog.content().add("$contributors");
            dialog.content().row();
            dialog.content().pane(new Table(){{
                int i = 0;
                left();
                for(Contributor c : contributors){
                    add("[lightgray]" + c.login).left().pad(3).padLeft(6).padRight(6);
                    if(++i % 3 == 0){
                        row();
                    }
                }
            }});
        }
        dialog.show();
    }
}
