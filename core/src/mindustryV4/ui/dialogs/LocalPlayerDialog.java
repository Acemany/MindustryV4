package mindustryV4.ui.dialogs;

import com.badlogic.gdx.utils.Scaling;
import mindustryV4.entities.Player;
import ucore.graphics.Draw;
import ucore.scene.ui.Image;
import ucore.scene.ui.layout.Stack;
import ucore.scene.ui.layout.Table;

import static mindustryV4.Vars.control;
import static mindustryV4.Vars.players;

public class LocalPlayerDialog extends FloatingDialog{

    public LocalPlayerDialog(){
        super("$addplayers");

        addCloseButton();
        shown(this::rebuild);
    }

    private void rebuild(){
        float size = 140f;

        content().clear();

        if(players.length > 1){
            content().addImageButton("icon-cancel", 14 * 2, () -> {
                control.removePlayer();
                rebuild();
            }).size(50f, size).pad(5).bottom();
        }else{
            content().add().size(50f, size);
        }

        for(Player player : players){
            Table table = new Table();
            Stack stack = new Stack();

            stack.add(new Image("button"));

            Image img = new Image(Draw.region("icon-chat"));
            img.setScaling(Scaling.fill);

            stack.add(img);

            table.add("Player " + (player.playerIndex + 1)).update(label -> label.setColor(player.color));
            table.row();
            table.add(stack).size(size);

            content().add(table).pad(5);
        }

        if(players.length < 4){
            content().addImageButton("icon-add", 14 * 2, () -> {
                control.addPlayer(players.length);
                rebuild();
            }).size(50f, size).pad(5).bottom();
        }
    }
}
