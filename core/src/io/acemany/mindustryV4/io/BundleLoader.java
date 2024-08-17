package io.acemany.mindustryV4.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import io.acemany.mindustryV4.Vars;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.core.Settings;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.util.Log;

import java.util.Locale;

import static io.acemany.mindustryV4.Vars.headless;

public class BundleLoader{

    public static void load(){
        Settings.defaults("locale", "default");
        Settings.load(Vars.appName, headless ? "io.acemany.mindustryV4.server" : "io.acemany.mindustryV4");
        loadBundle();
    }

    private static Locale getLocale(){
        String loc = Settings.getString("locale");
        if(loc.equals("default")){
            return Locale.getDefault();
        }else{
            Locale lastLocale;
            if(loc.contains("_")){
                String[] split = loc.split("_");
                lastLocale = new Locale(split[0], split[1]);
            }else{
                lastLocale = new Locale(loc);
            }

            return lastLocale;
        }
    }

    private static void loadBundle(){
        I18NBundle.setExceptionOnMissingKey(false);
        try{
            //try loading external bundle
            FileHandle handle = Gdx.files.local("bundle");

            Locale locale = Locale.ENGLISH;
            Core.bundle = I18NBundle.createBundle(handle, locale);

            Log.info("NOTE: external translation bundle has been loaded.");
            if(!headless){
                Timers.run(10f, () -> Vars.ui.showInfo("Note: You have successfully loaded an external translation bundle."));
            }
        }catch(Throwable e){
            //no external bundle found

            FileHandle handle = Gdx.files.internal("bundles/bundle");

            Locale locale = getLocale();
            Locale.setDefault(locale);
            if(!headless) Log.info("Got locale: {0}", locale);
            Core.bundle = I18NBundle.createBundle(handle, locale);
        }

    }
}
