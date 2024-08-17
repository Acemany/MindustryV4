package io.acemany.mindustryV4;

import io.acemany.mindustryV4.core.*;
import io.acemany.mindustryV4.game.EventType.GameLoadEvent;
import io.acemany.mindustryV4.io.BundleLoader;
import io.anuke.ucore.core.Events;
import io.anuke.ucore.core.Timers;
import io.anuke.ucore.modules.ModuleCore;
import io.anuke.ucore.util.Log;

import static io.acemany.mindustryV4.Vars.*;

public class Mindustry extends ModuleCore{

    @Override
    public void init(){
        Timers.mark();

        Vars.init();

        Log.setUseColors(false);
        BundleLoader.load();
        content.load();

        module(logic = new Logic());
        module(world = new World());
        module(control = new Control());
        module(renderer = new Renderer());
        module(ui = new UI());
        module(netServer = new NetServer());
        module(netClient = new NetClient());
    }

    @Override
    public void postInit(){
        Log.info("Time to load [total]: {0}", Timers.elapsed());
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void render(){
        threads.handleBeginRender();
        super.render();
        threads.handleEndRender();
    }

}
