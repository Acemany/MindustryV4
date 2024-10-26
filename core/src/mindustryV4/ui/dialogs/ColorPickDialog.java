package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import ucore.function.Consumer;
import ucore.scene.ui.Dialog;
import ucore.scene.ui.ImageButton;
import ucore.scene.ui.layout.Table;

import static mindustryV4.Vars.playerColors;
import static mindustryV4.Vars.players;

public class ColorPickDialog extends Dialog{
    private Consumer<Color> cons;

    public ColorPickDialog(){
        super("", "dialog");
        build();
    }

    private void build(){
        Table table = new Table();
        content().add(table);

        for(int i = 0; i < playerColors.length; i++){
            Color color = playerColors[i];

            ImageButton button = table.addImageButton("white", "clear-toggle", 34, () -> {
                cons.accept(color);
                hide();
            }).size(48).get();
            button.setChecked(players[0].color.equals(color));
            button.getStyle().imageUpColor = color;

            if(i % 4 == 3){
                table.row();
            }
        }

        keyDown(key -> {
            if(key == Keys.ESCAPE || key == Keys.BACK)
                hide();
        });

    }

    public void show(Consumer<Color> cons){
        this.cons = cons;
        show();
    }
}
