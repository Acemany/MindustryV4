package mindustryV4.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Align;
import mindustryV4.core.GameState.State;
import mindustryV4.game.EventType.ResizeEvent;
import mindustryV4.graphics.Palette;
import mindustryV4.net.Net;
import ucore.core.Core;
import ucore.core.Events;
import ucore.scene.ui.Dialog;
import ucore.scene.ui.ScrollPane;

import static mindustryV4.Vars.state;

public class FloatingDialog extends Dialog{
    private boolean wasPaused;
    protected boolean shouldPause;

    public FloatingDialog(String title){
        super(title, "dialog");
        setFillParent(true);
        title().setAlignment(Align.center);
        getTitleTable().row();
        getTitleTable().addImage("white", Palette.accent)
                .growX().height(3f).pad(4f);

        hidden(() -> {
            if(shouldPause && !state.is(State.menu)){
                if(!wasPaused || Net.active()){
                    state.set(State.playing);
                }
            }
        });

        shown(() -> {
            if(shouldPause && !state.is(State.menu)){
                wasPaused = state.is(State.paused);
                state.set(State.paused);
            }
        });

        boolean[] done = {false};

        shown(() -> Gdx.app.postRunnable(() ->
                forEach(child -> {
                    if(done[0]) return;

                    if(child instanceof ScrollPane){
                        Core.scene.setScrollFocus(child);
                        done[0] = true;
                    }
                })));
    }

    protected void onResize(Runnable run){
        Events.on(ResizeEvent.class, event -> {
            if(isShown()){
                run.run();
            }
        });
    }

    @Override
    public void addCloseButton(){
        buttons().addImageTextButton("$back", "icon-arrow-left", 30f, this::hide).size(230f, 64f);

        keyDown(key -> {
            if(key == Keys.ESCAPE || key == Keys.BACK) {
                Gdx.app.postRunnable(this::hide);
            }
        });
    }
}
