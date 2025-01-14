package mindustryV4.ui.fragments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import mindustryV4.core.GameState.State;
import mindustryV4.game.EventType.StateChangeEvent;
import mindustryV4.game.Team;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Palette;
import mindustryV4.net.Net;
import mindustryV4.net.Packets.AdminAction;
import mindustryV4.type.Recipe;
import mindustryV4.ui.IntFormat;
import mindustryV4.ui.Minimap;
import mindustryV4.ui.dialogs.FloatingDialog;
import ucore.core.*;
import ucore.graphics.Hue;
import ucore.scene.Element;
import ucore.scene.Group;
import ucore.scene.actions.Actions;
import ucore.scene.event.Touchable;
import ucore.scene.ui.Image;
import ucore.scene.ui.ImageButton;
import ucore.scene.ui.Label;
import ucore.scene.ui.TextButton;
import ucore.scene.ui.layout.Stack;
import ucore.scene.ui.layout.Table;
import ucore.scene.ui.layout.Unit;
import ucore.util.Bundles;
import ucore.util.Mathf;

import static mindustryV4.Vars.*;

public class HudFragment extends Fragment{
    public final PlacementFragment blockfrag = new PlacementFragment();

    private ImageButton menu, flip;
    private Stack wavetable;
    private Table infolabel;
    private Table lastUnlockTable;
    private Table lastUnlockLayout;
    private boolean shown = true;
    private float dsize = 58;
    private float isize = 40;

    private float coreAttackTime;
    private float lastCoreHP;
    private float coreAttackOpacity = 0f;

    public void build(Group parent){

        //menu at top left
        parent.fill(cont -> {

            cont.top().left().visible(() -> !state.is(State.menu));

            if(mobile){
                cont.table(select -> {
                    select.left();
                    select.defaults().size(dsize).left();

                    menu = select.addImageButton("icon-menu", "clear", isize, ui.paused::show).get();
                    flip = select.addImageButton("icon-arrow-up", "clear", isize, this::toggleMenus).get();

                    select.addImageButton("icon-pause", "clear", isize, () -> {
                        if(Net.active()){
                            ui.listfrag.toggle();
                        }else{
                            state.set(state.is(State.paused) ? State.playing : State.paused);
                        }
                    }).update(i -> {
                        if(Net.active()){
                            i.getStyle().imageUp = Core.skin.getDrawable("icon-players");
                        }else{
                            i.setDisabled(Net.active());
                            i.getStyle().imageUp = Core.skin.getDrawable(state.is(State.paused) ? "icon-play" : "icon-pause");
                        }
                    }).get();

                    select.addImageButton("icon-settings", "clear", isize, () -> {
                        if(Net.active() && mobile){
                            if(ui.chatfrag.chatOpen()){
                                ui.chatfrag.hide();
                            }else{
                                ui.chatfrag.toggle();
                            }
                        }else{
                            ui.unlocks.show();
                        }
                    }).update(i -> {
                        if(Net.active() && mobile){
                            i.getStyle().imageUp = Core.skin.getDrawable("icon-chat");
                        }else{
                            i.getStyle().imageUp = Core.skin.getDrawable("icon-unlocks");
                        }
                    }).get();

                    select.addImage("blank").color(Palette.accent).width(6f).fillY();
                });

                cont.row();
                cont.addImage("blank").height(6f).color(Palette.accent).fillX();
                cont.row();
            }

            cont.update(() -> {
                if(Inputs.keyTap("toggle_menus") && !ui.chatfrag.chatOpen()){
                    toggleMenus();
                }
            });

            Stack stack = new Stack();
            TextButton waves = new TextButton("", "wave");
            Table btable = new Table().margin(0);

            stack.add(waves);
            stack.add(btable);

            wavetable = stack;

            addWaveTable(waves);
            addPlayButton(btable);
            cont.add(stack).width(dsize * 4 + 6f);

            cont.row();

            //fps display
            infolabel = cont.table(t -> {
                IntFormat fps = new IntFormat("fps");
                IntFormat tps = new IntFormat("tps");
                IntFormat ping = new IntFormat("ping");
                t.label(() -> fps.get(Gdx.graphics.getFramesPerSecond())).padRight(10);
                t.row();
                if(Net.hasClient()){
                    t.label(() -> ping.get(Net.getPing())).visible(Net::client).colspan(2);
                }
            }).size(-1).visible(() -> Settings.getBool("fps")).update(t -> t.setTranslation(0, (!waves.isVisible() ? wavetable.getHeight() : Math.min(wavetable.getTranslation().y, wavetable.getHeight())) )).get();

            //make wave box appear below rest of menu
            if(mobile){
                cont.swapActor(wavetable, menu.getParent());
            }
        });

        //minimap
        parent.fill(t -> t.top().right().add(new Minimap())
            .visible(() -> !state.is(State.menu) && Settings.getBool("minimap")));

        //paused table
        parent.fill(t -> {
            t.top().visible(() -> state.is(State.paused) && !Net.active());
            t.table("button", top -> top.add("$paused").pad(6f));
        });

        parent.fill(t -> {
            t.visible(() -> netServer.isWaitingForPlayers() && !state.is(State.menu));
            t.table("button", c -> c.add("$waiting.players"));
        });

        //'core is under attack' table
        parent.fill(t -> {
            float notifDuration = 240f;

            Events.on(StateChangeEvent.class, event -> {
                if(event.to == State.menu || event.from == State.menu){
                    coreAttackTime = 0f;
                    lastCoreHP = Float.NaN;
                }
            });

            t.top().visible(() -> {
                if(state.is(State.menu) || state.teams.get(players[0].getTeam()).cores.size == 0 ||
                state.teams.get(players[0].getTeam()).cores.first().entity == null){
                    coreAttackTime = 0f;
                    return false;
                }

                float curr = state.teams.get(players[0].getTeam()).cores.first().entity.health;
                if(!Float.isNaN(lastCoreHP) && curr < lastCoreHP){
                    coreAttackTime = notifDuration;
                }
                lastCoreHP = curr;

                t.getColor().a = coreAttackOpacity;
                if(coreAttackTime > 0){
                    coreAttackOpacity = Mathf.lerpDelta(coreAttackOpacity, 1f, 0.1f);
                }else{
                    coreAttackOpacity = Mathf.lerpDelta(coreAttackOpacity, 0f, 0.1f);
                }

                coreAttackTime -= Timers.delta();

                return coreAttackOpacity > 0;
            });
            t.table("button", top -> top.add("$coreattack").pad(2)
            .update(label -> label.setColor(Hue.mix(Color.ORANGE, Color.SCARLET, Mathf.absin(Timers.time(), 2f, 1f)))));
        });

        //'saving' indicator
        parent.fill(t -> {
            t.bottom().visible(() -> !state.is(State.menu) && control.saves.isSaving());
            t.add("$saveload");
        });

        blockfrag.build(Core.scene.getRoot());
    }

    public void showToast(String text){
        Table table = new Table("button");
        table.update(() -> {
            if(state.is(State.menu)){
                table.remove();
            }
        });
        table.margin(12);
        table.addImage("icon-check").size(16*2).pad(3);
        table.add(text).wrap().width(280f).get().setAlignment(Align.center, Align.center);
        table.pack();

        //create container table which will align and move
        Table container = Core.scene.table();
        container.top().add(table);
        container.setTranslation(0, table.getPrefHeight());
        container.actions(Actions.translateBy(0, -table.getPrefHeight(), 1f, Interpolation.fade), Actions.delay(4f),
        //nesting actions() calls is necessary so the right prefHeight() is used
        Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interpolation.fade), Actions.removeActor())));
    }

    /**Show unlock notification for a new recipe.*/
    public void showUnlock(Recipe recipe){

        //if there's currently no unlock notification...
        if(lastUnlockTable == null){
            Table table = new Table("button");
            table.update(() -> {
                if(state.is(State.menu)){
                    table.remove();
                    lastUnlockLayout = null;
                    lastUnlockTable = null;
                }
            });
            table.margin(12);

            Table in = new Table();

            //create texture stack for displaying
            Stack stack = new Stack();
            for(TextureRegion region : recipe.result.getCompactIcon()){
                Image image = new Image(region);
                image.setScaling(Scaling.fit);
                stack.add(image);
            }

            in.add(stack).size(48f).pad(2);

            //add to table
            table.add(in).padRight(8);
            table.add("$unlocked");
            table.pack();

            //create container table which will align and move
            Table container = Core.scene.table();
            container.top().add(table);
            container.setTranslation(0, table.getPrefHeight());
            container.actions(Actions.translateBy(0, -table.getPrefHeight(), 1f, Interpolation.fade), Actions.delay(4f),
                    //nesting actions() calls is necessary so the right prefHeight() is used
                    Actions.run(() -> container.actions(Actions.translateBy(0, table.getPrefHeight(), 1f, Interpolation.fade), Actions.run(() -> {
                        lastUnlockTable = null;
                        lastUnlockLayout = null;
                    }), Actions.removeActor())));

            lastUnlockTable = container;
            lastUnlockLayout = in;
        }else{
            //max column size
            int col = 3;
            //max amount of elements minus extra 'plus'
            int cap = col * col - 1;

            //get old elements
            Array<Element> elements = new Array<>(lastUnlockLayout.getChildren());
            int esize = elements.size;

            //...if it's already reached the cap, ignore everything
            if(esize > cap) return;

            //get size of each element
            float size = 48f / Math.min(elements.size + 1, col);

            //correct plurals if needed
            if(esize == 1){
                ((Label) lastUnlockLayout.getParent().find(e -> e instanceof Label)).setText("$unlocked.plural");
            }

            lastUnlockLayout.clearChildren();
            lastUnlockLayout.defaults().size(size).pad(2);

            for(int i = 0; i < esize && i <= cap; i++){
                lastUnlockLayout.add(elements.get(i));

                if(i % col == col - 1){
                    lastUnlockLayout.row();
                }
            }

            //if there's space, add it
            if(esize < cap){

                Stack stack = new Stack();
                for(TextureRegion region : recipe.result.getCompactIcon()){
                    Image image = new Image(region);
                    image.setScaling(Scaling.fit);
                    stack.add(image);
                }

                lastUnlockLayout.add(stack);
            }else{ //else, add a specific icon to denote no more space
                lastUnlockLayout.addImage("icon-add");
            }

            lastUnlockLayout.pack();
        }
    }

    public void showTextDialog(String str){
        new FloatingDialog("$mission.info"){{
            shouldPause = true;
            setFillParent(false);
            getCell(content()).growX();
            content().margin(15).add(str).width(400f).wrap().get().setAlignment(Align.left, Align.left);
            buttons().addButton("$continue", this::hide).size(140, 60).pad(4);
        }}.show();
    }

    private void toggleMenus(){
        wavetable.clearActions();
        infolabel.clearActions();

        float dur = 0.3f;
        Interpolation in = Interpolation.pow3Out;

        if(flip != null){
            flip.getStyle().imageUp = Core.skin.getDrawable(shown ? "icon-arrow-down" : "icon-arrow-up");
        }

        if(shown){
            shown = false;
            blockfrag.toggle(dur, in);
            wavetable.actions(Actions.translateBy(0, (wavetable.getHeight() + Unit.dp.scl(dsize) + Unit.dp.scl(6)) - wavetable.getTranslation().y, dur, in));
            infolabel.actions(Actions.translateBy(0, (wavetable.getHeight()) - wavetable.getTranslation().y, dur, in));
        }else{
            shown = true;
            blockfrag.toggle(dur, in);
            wavetable.actions(Actions.translateBy(0, -wavetable.getTranslation().y, dur, in));
            infolabel.actions(Actions.translateBy(0, -infolabel.getTranslation().y, dur, in));
        }
    }

    private void addWaveTable(TextButton table){

        IntFormat wavef = new IntFormat("wave");
        IntFormat enemyf = new IntFormat("wave.enemy");
        IntFormat enemiesf = new IntFormat("wave.enemies");

        table.clearChildren();
        table.setTouchable(Touchable.enabled);

        table.labelWrap(() ->
            world.getSector() == null ?
                (state.enemies() > 0 && state.mode.disableWaveTimer ?
                wavef.get(state.wave) + "\n" + (state.enemies() == 1 ?
                    enemyf.get(state.enemies()) :
                    enemiesf.get(state.enemies())) :
                wavef.get(state.wave) + "\n" +
                    (!state.mode.disableWaveTimer ?
                    Bundles.format("wave.waiting", (int)(state.wavetime/60)) :
                    Bundles.get("waiting"))) :
            Bundles.format("mission.display", world.getSector().currentMission().displayString())).growX().pad(8f);

        table.clicked(() -> {
            if(world.getSector() != null && world.getSector().currentMission().hasMessage()){
                world.getSector().currentMission().showMessage();
            }
        });

        table.setDisabled(() -> !(world.getSector() != null && world.getSector().currentMission().hasMessage()));
        table.visible(() -> !((world.getSector() == null && state.mode.disableWaves) || !state.mode.showMission || (world.getSector() != null && world.getSector().complete)));
    }

    private void addPlayButton(Table table){
        table.right().addImageButton("icon-play", "right", 30f, () -> {
            if(Net.client() && players[0].isAdmin){
                Call.onAdminRequest(players[0], AdminAction.wave);
            }else{
                state.wavetime = 0f;
            }
        }).growY().fillX().right().width(40f).update(l -> {
            boolean vis = state.mode.disableWaveTimer && ((Net.server() || players[0].isAdmin) || !Net.active());
            boolean paused = state.is(State.paused) || !vis;

            l.getStyle().imageUp = Core.skin.getDrawable(vis ? "icon-play" : "clear");
            l.setTouchable(!paused ? Touchable.enabled : Touchable.disabled);
        }).visible(() -> state.mode.disableWaveTimer && ((Net.server() || players[0].isAdmin) || !Net.active()) && unitGroups[Team.red.ordinal()].size() == 0);
    }
}
