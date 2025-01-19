package mindustryV4.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import kryonet.*;
import mindustryV4.Mindustry;
import mindustryV4.core.Platform;
import mindustryV4.net.Net;

public class DesktopLauncher extends Lwjgl3Application{

    public DesktopLauncher(ApplicationListener listener, Lwjgl3ApplicationConfiguration config){
        super(listener, config);
    }

    public static void main(String[] arg){
        try{
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setTitle("Mindustry V4");
            config.setMaximized(true);
            config.setWindowedMode(960, 540);
            config.setWindowIcon("sprites/icon.png");

            Platform.instance = new DesktopPlatform(arg);

            Net.setClientProvider(new KryoClient());
            Net.setServerProvider(new KryoServer());
            new DesktopLauncher(new Mindustry(), config);
        }catch(Throwable e){
            CrashHandler.handle(e);
        }
    }
}
