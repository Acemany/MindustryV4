package mindustryV4.maps.missions;

import io.anuke.arc.Core;

import static mindustryV4.Vars.*;

/**A mission which simply runs a single action and is completed instantly.*/
public class ActionMission extends Mission{
    protected Runnable runner;

    public ActionMission(Runnable runner){
        this.runner = runner;
    }

    public ActionMission(){
    }

    @Override
    public void onComplete(){
        Core.app.post(runner);
    }

    @Override
    public boolean isComplete(){
        return true;
    }

    @Override
    public String displayString(){
        return Core.bundle.get("loading");
    }
}
