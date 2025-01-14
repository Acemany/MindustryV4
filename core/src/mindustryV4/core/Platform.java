package mindustryV4.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import ucore.core.Core;
import ucore.core.Settings;
import ucore.core.Timers;
import ucore.function.Consumer;
import ucore.scene.ui.Dialog;
import ucore.scene.ui.TextField;

import java.util.Random;

import static mindustryV4.Vars.mobile;

public abstract class Platform {
    /**Each separate game platform should set this instance to their own implementation.*/
    public static Platform instance = new Platform() {};

    /**Add a text input dialog that should show up after the field is tapped.*/
    public void addDialog(TextField field){
        addDialog(field, 16);
    }
    /**See addDialog().*/
    public void addDialog(TextField field, int maxLength){
        if(!mobile) return; //this is mobile only, desktop doesn't need dialogs

        field.tapped(() -> {
            Dialog dialog = new Dialog("", "dialog");
            dialog.setFillParent(true);
            dialog.content().top();
            dialog.content().defaults().height(65f);

            TextField[] use = {null};

            dialog.content().addImageButton("icon-copy", "clear", 16*3, () -> use[0].copy())
                    .visible(() -> !use[0].getSelection().isEmpty()).width(65f);

            dialog.content().addImageButton("icon-paste", "clear", 16*3, () ->
                    use[0].paste(Gdx.app.getClipboard().getContents(), false))
                    .visible(() -> !Gdx.app.getClipboard().getContents().isEmpty()).width(65f);

            TextField to = dialog.content().addField(field.getText(), t-> {}).pad(15).width(250f).get();
            to.setMaxLength(maxLength);
            to.keyDown(Keys.ENTER, () -> dialog.content().find("okb").fireClick());

            use[0] = to;

            dialog.content().addButton("$ok", () -> {
                field.clearText();
                field.appendText(to.getText());
                field.change();
                dialog.hide();
                Gdx.input.setOnscreenKeyboardVisible(false);
            }).width(90f).name("okb");

            dialog.show();
            Timers.runTask(1f, () -> {
                to.setCursorPosition(to.getText().length());
                Core.scene.setKeyboardFocus(to);
                Gdx.input.setOnscreenKeyboardVisible(true);
            });
        });
    }
    /**Called when the game is exited.*/
    public void onGameExit(){}

    /**Must be a base64 string 8 bytes in length.*/
    public String getUUID(){
        String uuid = Settings.getString("uuid", "");
        if(uuid.isEmpty()){
            byte[] result = new byte[8];
            new Random().nextBytes(result);
            uuid = new String(Base64Coder.encode(result));
            Settings.putString("uuid", uuid);
            Settings.save();
            return uuid;
        }
        return uuid;
    }
    /**Only used for iOS or android: open the share menu for a map or save.*/
    public void shareFile(FileHandle file){}

    /**Show a file chooser. Desktop only.
     *
     * @param text File chooser title text
     * @param content Description of the type of files to be loaded
     * @param cons Selection listener
     * @param open Whether to open or save files
     * @param filetype File extension to filter
     */
    public void showFileChooser(String text, String content, Consumer<FileHandle> cons, boolean open, String filetype){}

    /**Forces the app into landscape mode. Currently Android only.*/
    public void beginForceLandscape(){}

    /**Stops forcing the app into landscape orientation. Currently Android only.*/
    public void endForceLandscape(){}
}