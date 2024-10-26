package mindustryV4.server;

import mindustryV4.Vars;
import mindustryV4.core.Logic;
import mindustryV4.core.NetServer;
import mindustryV4.core.World;
import mindustryV4.game.Content;
import mindustryV4.io.BundleLoader;
import ucore.modules.ModuleCore;

import static mindustryV4.Vars.*;

public class MindustryServer extends ModuleCore{
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
        content.initialize(Content::init);

        module(logic = new Logic());
        module(world = new World());
        module(netServer = new NetServer());
        module(new ServerControl(args));
    }
}
