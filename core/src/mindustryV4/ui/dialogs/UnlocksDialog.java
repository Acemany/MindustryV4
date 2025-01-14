package mindustryV4.ui.dialogs;

import com.badlogic.gdx.utils.Array;
import mindustryV4.Vars;
import mindustryV4.game.Content;
import mindustryV4.game.UnlockableContent;
import mindustryV4.graphics.Palette;
import mindustryV4.type.ContentType;
import ucore.scene.event.HandCursorListener;
import ucore.scene.ui.Image;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.Tooltip;
import ucore.scene.ui.layout.Table;
import ucore.scene.utils.UIUtils;

import static mindustryV4.Vars.content;
import static mindustryV4.Vars.control;

public class UnlocksDialog extends FloatingDialog{

    public UnlocksDialog(){
        super("$unlocks");

        shouldPause = true;
        addCloseButton();
        shown(this::rebuild);
        onResize(this::rebuild);
    }

    void rebuild(){
        content().clear();

        Table table = new Table();
        table.margin(20);
        ScrollPane pane = new ScrollPane(table);

        Array<Content>[] allContent = content.getContentMap();

        for(int j = 0; j < allContent.length; j ++){
            ContentType type = ContentType.values()[j];

            Array<Content> array = allContent[j];
            if(array.size == 0 || !(array.first() instanceof UnlockableContent)) continue;

            table.add("$content." + type.name() + ".name").growX().left().color(Palette.accent);
            table.row();
            table.addImage("white").growX().pad(5).padLeft(0).padRight(0).height(3).color(Palette.accent);
            table.row();
            table.table(list -> {
                list.left();

                int maxWidth = UIUtils.portrait() ? 7 : 13;
                int size = 8 * 6;

                int count = 0;

                for(int i = 0; i < array.size; i++){
                    UnlockableContent unlock = (UnlockableContent) array.get(i);

                    if(unlock.isHidden()) continue;

                    Image image = control.unlocks.isUnlocked(unlock) ? new Image(unlock.getContentIcon()) : new Image("icon-locked");
                    image.addListener(new HandCursorListener());
                    list.add(image).size(size).pad(3);

                    if(control.unlocks.isUnlocked(unlock)){
                        image.clicked(() -> Vars.ui.content.show(unlock));
                        image.addListener(new Tooltip<>(new Table("button"){{
                            add(unlock.localizedName());
                        }}));
                    }

                    if((++count) % maxWidth == 0){
                        list.row();
                    }
                }
            }).growX().left().padBottom(10);
            table.row();
        }

        content().add(pane);
    }
}
