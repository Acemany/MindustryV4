package io.acemany.mindustryV4.server;

import io.acemany.mindustryV4.Vars;
import io.acemany.mindustryV4.core.Logic;
import io.acemany.mindustryV4.core.NetServer;
import io.acemany.mindustryV4.core.World;
import io.acemany.mindustryV4.game.Content;
import io.acemany.mindustryV4.io.BundleLoader;
import io.anuke.ucore.modules.ModuleCore;

import static io.acemany.mindustryV4.Vars.*;

public class MindustryServer extends ModuleCore{
    private final String[] args;

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
