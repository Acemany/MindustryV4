package mindustryV4;

import mindustryV4.core.*;
import mindustryV4.game.EventType.GameLoadEvent;
import mindustryV4.io.BundleLoader;
import io.anuke.arc.ApplicationCore;
import io.anuke.arc.Core;
import io.anuke.arc.Events;
import io.anuke.arc.util.Log;
import io.anuke.arc.util.Time;

import static mindustryV4.Vars.*;

public class Mindustry extends ApplicationCore{

    @Override
    public void setup(){
        Time.setDeltaProvider(() -> {
            float result = Core.graphics.getDeltaTime() * 60f;
            return Float.isNaN(result) || Float.isInfinite(result) ? 1f : Math.min(result, 60f / 10f);
        });

        Time.mark();

        Vars.init();

        Log.setUseColors(false);
        BundleLoader.load();
        content.load();

        add(logic = new Logic());
        add(world = new World());
        add(control = new Control());
        add(renderer = new Renderer());
        add(ui = new UI());
        add(netServer = new NetServer());
        add(netClient = new NetClient());
    }

    @Override
    public void init(){
        super.init();

        Log.info("Time to load [total]: {0}", Time.elapsed());
        Events.fire(new GameLoadEvent());
    }

    @Override
    public void update(){
        long lastFrameTime = Time.millis();

        super.update();

        int fpsCap = Core.settings.getInt("fpscap", 125);

        if(fpsCap <= 120){
            long target = 1000 / fpsCap;
            long elapsed = Time.timeSinceMillis(lastFrameTime);
            if(elapsed < target){
                try{
                    Thread.sleep(target - elapsed);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
