package mindustryV4.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import mindustryV4.Vars;
import ucore.core.*;
import ucore.util.Log;

import java.util.Locale;

import static mindustryV4.Vars.headless;

public class BundleLoader{

    public static void load(){
        Settings.defaults("locale", "default");
        Settings.load(Vars.appName, headless ? "mindustryV4.server" : "mindustryV4");
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

            //router
            if(locale.toString().equals("router")){
                Core.bundle.debug("router");
            }
        }

    }
}
