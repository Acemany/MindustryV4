package mindustryV4.ui.fragments;

import mindustryV4.graphics.Palette;
import ucore.scene.Group;
import ucore.scene.event.Touchable;
import ucore.scene.ui.Label;
import ucore.scene.ui.TextButton;
import ucore.scene.ui.layout.Table;

public class LoadingFragment extends Fragment{
    private Table table;
    private TextButton button;

    @Override
    public void build(Group parent){
        parent.fill("loadDim", t -> {
            t.setVisible(false);
            t.setTouchable(Touchable.enabled);
            t.add().height(70f).row();

            t.addImage("white").growX().height(3f).pad(4f).growX().get().setColor(Palette.accent);
            t.row();
            t.add("$loading").name("namelabel").pad(10f);
            t.row();
            t.addImage("white").growX().height(3f).pad(4f).growX().get().setColor(Palette.accent);
            t.row();

            button = t.addButton("$cancel", () -> {}).pad(20).size(250f, 70f).visible(false).get();
            table = t;
        });
    }

    public void setButton(Runnable listener){
        button.setVisible(true);
        button.getListeners().removeIndex(button.getListeners().size - 1);
        button.clicked(listener);
    }

    public void show(){
        show("$loading");
    }

    public void show(String text){
        table.<Label>find("namelabel").setText(text);
        table.setVisible(true);
        table.toFront();
    }

    public void hide(){
        table.setVisible(false);
        button.setVisible(false);
    }
}
