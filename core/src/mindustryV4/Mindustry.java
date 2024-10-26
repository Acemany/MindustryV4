package mindustryV4;

import mindustryV4.core.*;
import mindustryV4.game.EventType.GameLoadEvent;
import mindustryV4.io.BundleLoader;
import ucore.core.Events;
import ucore.core.Timers;
import ucore.modules.ModuleCore;
import ucore.util.Log;

import static mindustryV4.Vars.*;

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
