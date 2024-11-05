package mindustryV4.maps.missions;

import mindustryV4.game.UnlockableContent;
import io.anuke.arc.Core;

public class ContentMission extends Mission {
    private final UnlockableContent content;
    private boolean done;

    public ContentMission(UnlockableContent content) {
        this.content = content;
    }

    @Override
    public void onContentUsed(UnlockableContent content) {
        if(content == this.content){
            done = true;
        }
    }

    @Override
    public boolean isComplete() {
        return done;
    }

    @Override
    public void reset() {
        done = false;
    }

    @Override
    public String displayString() {
        return Core.bundle.format("mission.create", content.localizedName());
    }
}
