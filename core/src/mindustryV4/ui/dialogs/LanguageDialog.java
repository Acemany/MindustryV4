package mindustryV4.ui.dialogs;

import ucore.core.Settings;
import ucore.scene.ui.ButtonGroup;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.TextButton;
import ucore.scene.ui.layout.Table;
import ucore.util.Log;

import java.util.Locale;

import static mindustryV4.Vars.locales;
import static mindustryV4.Vars.ui;

public class LanguageDialog extends FloatingDialog{
    private Locale lastLocale;

    public LanguageDialog(){
        super("$settings.language");
        addCloseButton();
        setup();
    }

    private void setup(){
        Table langs = new Table();
        langs.marginRight(24f).marginLeft(24f);
        ScrollPane pane = new ScrollPane(langs);
        pane.setFadeScrollBars(false);

        ButtonGroup<TextButton> group = new ButtonGroup<>();

        for(Locale loc : locales){
            TextButton button = new TextButton(loc.getDisplayName(loc), "toggle");
            button.clicked(() -> {
                if(getLocale().equals(loc)) return;
                Settings.putString("locale", loc.toString());
                Settings.save();
                Log.info("Setting locale: {0}", loc.toString());
                ui.showInfo("$language.restart");
            });
            langs.add(button).group(group).update(t -> t.setChecked(loc.equals(getLocale()))).size(400f, 50f).pad(2).row();
        }

        content().add(pane);
    }

    public Locale getLocale(){
        String loc = Settings.getString("locale");

        if(loc.equals("default")){
            findClosestLocale();
        }

        if(lastLocale == null || !lastLocale.toString().equals(loc)){
            if(loc.contains("_")){
                String[] split = loc.split("_");
                lastLocale = new Locale(split[0], split[1]);
            }else{
                lastLocale = new Locale(loc);
            }
        }

        return lastLocale;
    }

    void findClosestLocale(){
        //check exact locale
        for(Locale l : locales){
            if(l.equals(Locale.getDefault())){
                Settings.putString("locale", l.toString());
                return;
            }
        }

        //find by language
        for(Locale l : locales){
            if(l.getLanguage().equals(Locale.getDefault().getLanguage())){
                Settings.putString("locale", l.toString());
                return;
            }
        }

        Settings.putString("locale", new Locale("en").toString());
    }
}
