package io.acemany.mindustryV4.content.fx;

import io.acemany.mindustryV4.game.ContentList;
import io.acemany.mindustryV4.type.ContentType;

public abstract class FxList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.effect;
    }
}
