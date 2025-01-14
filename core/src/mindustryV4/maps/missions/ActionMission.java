package mindustryV4.maps.missions;

import ucore.util.Bundles;

import static mindustryV4.Vars.threads;

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
        threads.run(runner);
    }

    @Override
    public boolean isComplete(){
        return true;
    }

    @Override
    public String displayString(){
        return Bundles.get("loading");
    }
}
