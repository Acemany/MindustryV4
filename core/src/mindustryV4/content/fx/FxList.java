package mindustryV4.content.fx;

import mindustryV4.game.ContentList;
import mindustryV4.type.ContentType;

public abstract class FxList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.effect;
    }
}
