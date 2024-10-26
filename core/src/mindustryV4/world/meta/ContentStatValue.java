package mindustryV4.world.meta;

import mindustryV4.game.UnlockableContent;

public interface ContentStatValue extends StatValue{
    UnlockableContent[] getValueContent();
}
