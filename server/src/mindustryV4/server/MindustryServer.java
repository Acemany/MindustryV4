package mindustryV4.server;

import io.anuke.arc.*;
import mindustryV4.Vars;
import mindustryV4.core.Logic;
import mindustryV4.core.NetServer;
import mindustryV4.core.World;
import mindustryV4.game.Content;
import mindustryV4.io.BundleLoader;

import static mindustryV4.Vars.*;

public class MindustryServer implements ApplicationListener{
    private String[] args;

    public MindustryServer(String[] args){
        this.args = args;
    }

    @Override
    public void init(){
        Vars.init();

        headless = true;

        BundleLoader.load();
        content.verbose(false);
        content.load();

        Core.app.addListener(logic = new Logic());
        Core.app.addListener(world = new World());
        Core.app.addListener(netServer = new NetServer());
        Core.app.addListener(new ServerControl(args));

        content.initialize(Content::init);
    }
}
