package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import mindustryV4.Vars;
import mindustryV4.io.Changelogs;
import mindustryV4.io.Changelogs.VersionInfo;
import mindustryV4.game.Version;
import ucore.core.Settings;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.layout.Table;
import ucore.util.Log;
import ucore.util.OS;

import static mindustryV4.Vars.ios;

public class ChangelogDialog extends FloatingDialog{
    private final float vw = 600;
    private Array<VersionInfo> versions;

    public ChangelogDialog(){
        super("$changelog.title");

        addCloseButton();

        content().add("$changelog.loading");

        if(!ios && !OS.isMac){
            Changelogs.getChangelog(result -> {
                versions = result;
                Gdx.app.postRunnable(this::setup);
            }, t -> {
                Log.err(t);
                Gdx.app.postRunnable(this::setup);
            });
        }
    }

    void setup(){
        Table table = new Table();
        ScrollPane pane = new ScrollPane(table);

        content().clear();
        content().add(pane).grow();

        if(versions == null){
            table.add("$changelog.error");
            if(Vars.android){
                table.row();
                table.add("$changelog.error.android").padTop(8);
            }

            if(ios){
                table.row();
                table.add("$changelog.error.ios").padTop(8);
            }
        }else{
            for(VersionInfo info : versions){
                String desc = info.description;

                desc = desc.replace("Android", "Mobile");

                Table in = new Table("underline");
                in.top().left().margin(10);

                in.add("[accent]" + info.name + "[LIGHT_GRAY]  | " + info.date);
                if(info.build == Version.build){
                    in.row();
                    in.add("$changelog.current");
                }else if(info == versions.first()){
                    in.row();
                    in.add("$changelog.latest");
                }
                in.row();
                in.labelWrap("[lightgray]" + desc).width(vw - 20).padTop(12);

                table.add(in).width(vw).pad(8).row();
            }

            int lastid = Settings.getInt("lastBuild");
            if(lastid != 0 && versions.peek().build > lastid){
                Settings.putInt("lastBuild", versions.peek().build);
                Settings.save();
                show();
            }
        }
    }
}
