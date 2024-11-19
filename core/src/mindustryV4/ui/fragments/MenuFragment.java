package mindustryV4.ui.fragments;

import io.anuke.arc.Core;
import io.anuke.arc.Events;
import io.anuke.arc.scene.Group;
import io.anuke.arc.scene.ui.layout.Table;
import io.anuke.arc.util.Strings;
import mindustryV4.core.GameState.State;
import mindustryV4.core.Platform;
import mindustryV4.game.EventType.ResizeEvent;
import mindustryV4.game.Version;
import mindustryV4.ui.MenuButton;
import mindustryV4.ui.MobileButton;
import mindustryV4.ui.dialogs.FloatingDialog;

import static mindustryV4.Vars.*;

public class MenuFragment extends Fragment{
    private Table container;

    @Override
    public void build(Group parent){
        parent.fill(c -> {
            container = c;
            container.visible(() -> state.is(State.menu));

            if(!mobile){
                buildDesktop();
            }else{
                buildMobile();
                Events.on(ResizeEvent.class, event -> buildMobile());
            }
        });

        //telegram icon in top right
        parent.fill(c -> c.top().right().addButton("", "telegram", ui.telegram::show).size(84, 45)
        .visible(() -> state.is(State.menu)));

        //info icon
        if(mobile){
            parent.fill(c -> c.top().left().addButton("", "info", ui.about::show).size(84, 45)
            .visible(() -> state.is(State.menu)));
        }

        //version info
        parent.fill(c -> c.bottom().left().add(Strings.formatArgs("Mindustry v{0} {1}-{2} {3}{4}", Version.number, Version.modifier, Version.type,
        (Version.build == -1 ? "custom build" : "build " + Version.build), Version.revision == 0 ? "" : "." + Version.revision))
        .visible(() -> state.is(State.menu)));
    }

    private void buildMobile(){
        container.clear();
        container.setSize(Core.graphics.getWidth(), Core.graphics.getHeight());

        float size = 120f;
        float isize = 14f * 4;
        container.defaults().size(size).pad(5).padTop(4f);

        MobileButton
        play = new MobileButton("icon-play-2", isize, "$play", ui.sectors::show),
        maps = new MobileButton("icon-map", isize, "$maps", ui.maps::show),
        custom = new MobileButton("icon-play-custom", isize, "$customgame", this::showCustomSelect),
        join = new MobileButton("icon-add", isize, "$joingame", ui.join::show),
        editor = new MobileButton("icon-editor", isize, "$editor", () -> ui.loadAnd(ui.editor::show)),
        tools = new MobileButton("icon-tools", isize, "$settings", ui.settings::show),
        tech = new MobileButton("icon-tree", isize, "$techtree", ui.tech::show);

        if(Core.graphics.getWidth() > Core.graphics.getHeight()){
            container.add(play);
            container.add(join);
            container.add(custom);
            container.add(maps);
            container.row();

            container.table(table -> {
                table.defaults().set(container.defaults());

                table.add(editor);
                table.add(tools);
                table.add(tech);
            }).colspan(4);
        }else{
            container.add(play);
            container.add(maps);
            container.row();
            container.add(custom);
            container.add(join);
            container.row();
            container.add(editor);
            container.add(tools);
            container.row();

            container.table(table -> {
                table.defaults().set(container.defaults());

                table.add(tech);
            }).colspan(2);
        }
    }

    private void buildDesktop(){
        container.table(out -> {

            float w = 200f;
            float bw = w * 2f + 10f;

            out.margin(16);
            out.defaults().size(w, 66f).padTop(5).padRight(5);

            out.add(new MenuButton("icon-play-2", "$play", ui.sectors::show)).width(bw).colspan(2);

            out.row();

            out.add(new MenuButton("icon-add", "$joingame", ui.join::show));

            out.add(new MenuButton("icon-play-custom", "$customgame", this::showCustomSelect));

            out.row();

            out.add(new MenuButton("icon-editor", "$editor", () -> ui.loadAnd(ui.editor::show)));

            out.add(new MenuButton("icon-map", "$maps", ui.maps::show));

            out.row();

            out.add(new MenuButton("icon-info", "$about.button", ui.about::show));

            out.add(new MenuButton("icon-tools", "$settings", ui.settings::show));

            out.row();

            out.add(new MenuButton("icon-exit", "$quit", Core.app::exit)).width(bw).colspan(2);
        });
    }

    private void showCustomSelect(){
        FloatingDialog dialog = new FloatingDialog("$play");
        dialog.setFillParent(false);
        dialog.addCloseButton();
        dialog.cont.defaults().size(230f, 64f);
        dialog.cont.add(new MenuButton("icon-editor", "$newgame", () -> {
            dialog.hide();
            ui.custom.show();
        }));
        dialog.cont.row();
        dialog.cont.add(new MenuButton("icon-load", "$loadgame", () -> {
            ui.load.show();
            dialog.hide();
        }));
        dialog.show();
    }
}