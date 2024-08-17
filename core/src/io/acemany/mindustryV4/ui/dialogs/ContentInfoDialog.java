package io.acemany.mindustryV4.ui.dialogs;

import io.acemany.mindustryV4.game.UnlockableContent;
import io.anuke.ucore.scene.ui.ScrollPane;
import io.anuke.ucore.scene.ui.layout.Table;

public class ContentInfoDialog extends FloatingDialog{

    public ContentInfoDialog(){
        super("$text.info.title");

        addCloseButton();
    }

    public void show(UnlockableContent content){
        content().clear();

        Table table = new Table();
        table.margin(10);

        content.displayInfo(table);

        ScrollPane pane = new ScrollPane(table);
        content().add(pane);

        show();
    }
}
