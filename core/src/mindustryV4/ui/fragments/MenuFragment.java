package mindustryV4.ui.fragments;

import com.badlogic.gdx.Gdx;
import mindustryV4.core.GameState.State;
import mindustryV4.core.Platform;
import mindustryV4.game.EventType.ResizeEvent;
import mindustryV4.game.Version;
import mindustryV4.ui.MenuButton;
import mindustryV4.ui.MobileButton;
import mindustryV4.ui.dialogs.FloatingDialog;
import ucore.core.Events;
import ucore.scene.Group;
import ucore.scene.ui.layout.Table;
import ucore.util.Strings;

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
        container.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float size = 120f;
        float isize = 14f * 4;
        container.defaults().size(size).pad(5).padTop(4f);

        MobileButton
            play = new MobileButton("icon-play-2", isize, "$sectors", ui.sectors::show),
            load = new MobileButton("icon-load", isize, "$load", ui.load::show),
            custom = new MobileButton("icon-map", isize, "$customgame", ui.levels::show),
            join = new MobileButton("icon-add", isize, "$joingame", ui.join::show),
            //I don't like current editor.
            //editor = new MobileButton("icon-editor", isize, "$editor", () -> ui.loadGraphics(ui.editor::show)),
            editor = new MobileButton("icon-editor", isize, "$maps", ui.maps::show),
            tools = new MobileButton("icon-tools", isize, "$settings", ui.settings::show),
            unlocks = new MobileButton("icon-unlocks", isize, "$unlocks", ui.unlocks::show),
            quit = new MobileButton("icon-exit", isize, "$quit", Gdx.app::exit);

        if(Gdx.graphics.getWidth() > Gdx.graphics.getHeight()){
            container.add(play);
            container.add(join);
            container.add(custom);
            container.add(load);
            container.row();

            container.table(table -> {
                table.defaults().set(container.defaults());

                table.add(editor);
                table.add(tools);
                table.add(unlocks);
                table.add(quit);
            }).colspan(4);
        }else{
            container.add(play);
            container.add(load);
            container.row();
            container.add(custom);
            container.add(join);
            container.row();
            container.add(editor);
            container.add(tools);
            container.row();

            container.table(table -> {
                table.defaults().set(container.defaults());

                table.add(unlocks);
                table.add(quit);
            }).colspan(2);
        }
    }

    private void buildDesktop(){
        container.table(out -> {

            float w = 200f;
            float bw = w * 2f + 10f;

            out.margin(16);
            out.defaults().size(w, 66f).padTop(5).padRight(5);

            out.add(new MenuButton("icon-play-2", "$play", MenuFragment.this::showPlaySelect)).width(bw).colspan(2);
            out.row();

            out.add(new MenuButton("icon-editor", "$editor", () -> ui.loadGraphics(ui.editor::show)));
            out.add(new MenuButton("icon-map", "$maps", ui.maps::show));
            out.row();

            out.add(new MenuButton("icon-info", "$about.button", ui.about::show));
            out.add(new MenuButton("icon-tools", "$settings", ui.settings::show));
            out.row();

            out.add(new MenuButton("icon-menu", "$changelog.title", ui.changelog::show));
            out.add(new MenuButton("icon-unlocks", "$unlocks", ui.unlocks::show));
            out.row();

            out.add(new MenuButton("icon-exit", "$quit", Gdx.app::exit)).width(bw).colspan(2);
        });
    }

    private void showPlaySelect(){
        float w = 220f;
        float bw = w * 2f + 10f;

        FloatingDialog dialog = new FloatingDialog("$play");
        dialog.addCloseButton();
        dialog.content().defaults().height(66f).width(w).padRight(5f);

        dialog.content().add(new MenuButton("icon-play-2", "$sectors", () -> {
            dialog.hide();
            ui.sectors.show();
        })).width(bw).colspan(2);
        dialog.content().row();

        dialog.content().add(new MenuButton("icon-add", "$joingame", () -> {
            ui.join.show();
            dialog.hide();
        }));

        dialog.content().add(new MenuButton("icon-editor", "$customgame", () -> {
            dialog.hide();
            ui.levels.show();
        }));

        dialog.content().row();

        dialog.content().add(new MenuButton("icon-load", "$loadgame", () -> {
            ui.load.show();
            dialog.hide();
        })).width(bw).colspan(2);

        dialog.show();
    }
}
