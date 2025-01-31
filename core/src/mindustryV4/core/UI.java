package mindustryV4.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Align;
import mindustryV4.editor.MapEditorDialog;
import mindustryV4.game.EventType.ResizeEvent;
import mindustryV4.graphics.Palette;
import mindustryV4.input.InputHandler;
import mindustryV4.ui.dialogs.*;
import mindustryV4.ui.fragments.*;
import ucore.core.*;
import ucore.function.Consumer;
import ucore.graphics.Draw;
import ucore.modules.SceneModule;
import ucore.scene.Group;
import ucore.scene.Skin;
import ucore.scene.actions.Actions;
import ucore.scene.ui.Dialog;
import ucore.scene.ui.TextField;
import ucore.scene.ui.TextField.TextFieldFilter;
import ucore.scene.ui.TooltipManager;
import ucore.scene.ui.layout.Table;
import ucore.scene.ui.layout.Unit;
import ucore.util.Strings;

import static mindustryV4.Vars.*;
import static ucore.scene.actions.Actions.*;

public class UI extends SceneModule{
    private FreeTypeFontGenerator generator;

    public final MenuFragment menufrag = new MenuFragment();
    public final HudFragment hudfrag = new HudFragment();
    public final ChatFragment chatfrag = new ChatFragment();
    public final PlayerListFragment listfrag = new PlayerListFragment();
    public final BackgroundFragment backfrag = new BackgroundFragment();
    public final LoadingFragment loadfrag = new LoadingFragment();

    public AboutDialog about;
    public RestartDialog restart;
    public CustomGameDialog levels;
    public MapsDialog maps;
    public LoadDialog load;
    public TelegramDialog telegram;
    public JoinDialog join;
    public HostDialog host;
    public PausedDialog paused;
    public SettingsMenuDialog settings;
    public ControlsDialog controls;
    public MapEditorDialog editor;
    public LanguageDialog language;
    public BansDialog bans;
    public AdminsDialog admins;
    public TraceDialog traces;
    public ChangelogDialog changelog;
    public LocalPlayerDialog localplayers;
    public UnlocksDialog unlocks;
    public ContentInfoDialog content;
    public SectorsDialog sectors;
    public MissionDialog missions;

    public UI(){
        Dialog.setShowAction(() -> sequence(
            alpha(0f),
            originCenter(),
            moveToAligned(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Align.center),
            scaleTo(0.0f, 1f),
            parallel(
                scaleTo(1f, 1f, 0.1f, Interpolation.fade),
                fadeIn(0.1f, Interpolation.fade)
            )
        ));

        Dialog.setHideAction(() -> sequence(
            parallel(
                scaleTo(0.01f, 0.01f, 0.1f, Interpolation.fade),
                fadeOut(0.1f, Interpolation.fade)
            )
        ));

        TooltipManager.getInstance().animations = false;

        Settings.setErrorHandler(() -> Timers.run(1f, () -> showError("[crimson]Failed to access local storage.\nSettings will not be saved.")));

        Dialog.closePadR = -1;
        Dialog.closePadT = 5;

        Colors.put("accent", Palette.accent);
    }
    
    void generateFonts(){
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"));
        FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = (int)(14*2 * Math.max(Unit.dp.scl(1f), 0.5f));
        param.shadowColor = Color.DARK_GRAY;
        param.shadowOffsetY = 2;
        param.incremental = true;

        skin.add("default-font", generator.generateFont(param));
        skin.add("default-font-chat", generator.generateFont(param));
        skin.getFont("default-font").getData().markupEnabled = true;
        skin.getFont("default-font").setOwnsTexture(false);
    }

    @Override
    protected void loadSkin(){
        skin = new Skin(Core.atlas);
        generateFonts();
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        for(BitmapFont font : skin.getAll(BitmapFont.class).values()){
            font.setUseIntegerPositions(true);
            //font.getData().setScale(Vars.fontScale);
        }
    }

    @Override
    public void update(){
        if(disableUI) return;

        if(Graphics.drawing()) Graphics.end();

        act();

        Graphics.begin();

        for(int i = 0; i < players.length; i++){
            InputHandler input = control.input(i);

            if(input.isCursorVisible()){
                Draw.color();

                float scl = Unit.dp.scl(3f);

                Draw.rect("controller-cursor", input.getMouseX(), Gdx.graphics.getHeight() - input.getMouseY(), 16 * scl, 16 * scl);
            }
        }

        Graphics.end();
        Draw.color();
    }

    @Override
    public void init(){
        editor = new MapEditorDialog();
        controls = new ControlsDialog();
        restart = new RestartDialog();
        join = new JoinDialog();
        telegram = new TelegramDialog();
        load = new LoadDialog();
        levels = new CustomGameDialog();
        language = new LanguageDialog();
        unlocks = new UnlocksDialog();
        settings = new SettingsMenuDialog();
        host = new HostDialog();
        paused = new PausedDialog();
        changelog = new ChangelogDialog();
        about = new AboutDialog();
        bans = new BansDialog();
        admins = new AdminsDialog();
        traces = new TraceDialog();
        maps = new MapsDialog();
        localplayers = new LocalPlayerDialog();
        content = new ContentInfoDialog();
        sectors = new SectorsDialog();
        missions = new MissionDialog();

        Group group = Core.scene.getRoot();

        backfrag.build(group);
        control.input(0).getFrag().build(Core.scene.getRoot());
        hudfrag.build(group);
        menufrag.build(group);
        chatfrag.container().build(group);
        listfrag.build(group);
        loadfrag.build(group);
    }

    @Override
    public void resize(int width, int height){
        super.resize(width, height);

        Events.fire(new ResizeEvent());
    }

    @Override
    public void dispose(){
        super.dispose();
        generator.dispose();
    }

    public void loadGraphics(Runnable call){
        loadGraphics("$loading", call);
    }

    public void loadGraphics(String text, Runnable call){
        loadfrag.show(text);
        Timers.runTask(7f, () -> {
            call.run();
            loadfrag.hide();
        });
    }

    public void loadLogic(Runnable call){
        loadLogic("$loading", call);
    }

    public void loadLogic(String text, Runnable call){
        loadfrag.show(text);
        Timers.runTask(7f, () ->
            threads.run(() -> {
                call.run();
                threads.runGraphics(loadfrag::hide);
            }));
    }

    public void showTextInput(String title, String text, String def, TextFieldFilter filter, Consumer<String> confirmed){
        new Dialog(title, "dialog"){{
            content().margin(30).add(text).padRight(6f);
            TextField field = content().addField(def, t -> {
            }).size(170f, 50f).get();
            field.setTextFieldFilter((f, c) -> field.getText().length() < 12 && filter.acceptChar(f, c));
            Platform.instance.addDialog(field);
            buttons().defaults().size(120, 54).pad(4);
            buttons().addButton("$ok", () -> {
                confirmed.accept(field.getText());
                hide();
            }).disabled(b -> field.getText().isEmpty());
            buttons().addButton("$cancel", this::hide);
        }}.show();
    }

    public void showTextInput(String title, String text, String def, Consumer<String> confirmed){
        showTextInput(title, text, def, (field, c) -> true, confirmed);
    }

    public void showInfoFade(String info){
        Table table = new Table();
        table.setFillParent(true);
        table.actions(Actions.fadeOut(7f, Interpolation.fade), Actions.removeActor());
        table.top().add(info).padTop(8);
        Core.scene.add(table);
    }

    public void showInfo(String info){
        new Dialog("", "dialog"){{
            getCell(content()).growX();
            content().margin(15).add(info).width(400f).wrap().get().setAlignment(Align.center, Align.center);
            buttons().addButton("$ok", this::hide).size(90, 50).pad(4);
        }}.show();
    }

    public void showInfo(String info, Runnable clicked){
        new Dialog("", "dialog"){{
            getCell(content()).growX();
            content().margin(15).add(info).width(400f).wrap().get().setAlignment(Align.center, Align.center);
            buttons().addButton("$ok", () -> {
                clicked.run();
                hide();
            }).size(90, 50).pad(4);
        }}.show();
    }

    public void showError(String text){
        new Dialog("$error.title", "dialog"){{
            content().margin(15).add(text).width(400f).wrap().get().setAlignment(Align.center, Align.center);
            buttons().addButton("$ok", this::hide).size(90, 50).pad(4);
        }}.show();
    }

    public void showText(String title, String text){
        new Dialog(title, "dialog"){{
            content().margin(15).add(text).width(400f).wrap().get().setAlignment(Align.center, Align.center);
            buttons().addButton("$ok", this::hide).size(90, 50).pad(4);
        }}.show();
    }

    public void showConfirm(String title, String text, Runnable confirmed){
        FloatingDialog dialog = new FloatingDialog(title);
        dialog.content().add(text).width(400f).wrap().pad(4f).get().setAlignment(Align.center, Align.center);
        dialog.buttons().defaults().size(200f, 54f).pad(2f);
        dialog.setFillParent(false);
        dialog.buttons().addButton("$cancel", dialog::hide);
        dialog.buttons().addButton("$ok", () -> {
            dialog.hide();
            confirmed.run();
        });
        dialog.keyDown(Keys.ESCAPE, dialog::hide);
        dialog.keyDown(Keys.BACK, dialog::hide);
        dialog.show();
    }

    public String formatAmount(int number){
        if(number >= 1000000){
            return Strings.toFixed(number / 1000000f, 1) + "[gray]mil[]";
        }else if(number >= 10000){
            return number / 1000 + "[gray]k[]";
        }else if(number >= 1000){
            return Strings.toFixed(number / 1000f, 1) + "[gray]k[]";
        }else{
            return number + "";
        }
    }
}
