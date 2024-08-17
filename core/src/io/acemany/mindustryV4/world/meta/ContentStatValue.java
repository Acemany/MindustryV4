package io.acemany.mindustryV4.world.meta;

import io.acemany.mindustryV4.game.UnlockableContent;

public interface ContentStatValue extends StatValue{
    UnlockableContent[] getValueContent();
}
