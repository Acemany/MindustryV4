package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Align;
import mindustryV4.graphics.Palette;
import ucore.scene.ui.Image;
import ucore.scene.ui.KeybindDialog;

public class ControlsDialog extends KeybindDialog{

    public ControlsDialog(){
        setDialog();

        setFillParent(true);
        title().setAlignment(Align.center);
        getTitleTable().row();
        getTitleTable().add(new Image("white"))
                .growX().height(3f).pad(4f).get().setColor(Palette.accent);
    }

    @Override
    public void addCloseButton(){
        buttons().addImageTextButton("$back", "icon-arrow-left", 30f, this::hide).size(230f, 64f);

        keyDown(key -> {
            if(key == Keys.ESCAPE || key == Keys.BACK)
                hide();
        });
    }
}
