package mindustryV4.io;

import io.anuke.arc.Core;
import io.anuke.arc.files.FileHandle;
import io.anuke.arc.util.I18NBundle;
import mindustryV4.Vars;
import io.anuke.arc.util.Time;
import io.anuke.arc.util.Log;
import mindustryV4.input.*;

import java.util.Locale;

import static mindustryV4.Vars.headless;

public class BundleLoader{

    public static void load(){
        Core.settings.defaults("locale", "default");
        Core.keybinds.setDefaults(Binding.values());
        Core.settings.load();
        loadBundle();
    }

    private static Locale getLocale(){
        String loc = Core.settings.getString("locale");
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
        try{
            //try loading external bundle
            FileHandle handle = Core.files.local("bundle");

            Locale locale = Locale.ENGLISH;
            Core.bundle = I18NBundle.createBundle(handle, locale);

            Log.info("NOTE: external translation bundle has been loaded.");
            if(!headless){
                Time.run(10f, () -> Vars.ui.showInfo("Note: You have successfully loaded an external translation bundle."));
            }
        }catch(Throwable e){
            //no external bundle found

            FileHandle handle = Core.files.internal("bundles/bundle");

            Locale locale = getLocale();
            Locale.setDefault(locale);
            if(!headless) Log.info("Got locale: {0}", locale);
            Core.bundle = I18NBundle.createBundle(handle, locale);
        }
    }
}
