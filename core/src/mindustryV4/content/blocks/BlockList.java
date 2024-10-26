package mindustryV4.content.blocks;

import mindustryV4.game.ContentList;
import mindustryV4.type.ContentType;

public abstract class BlockList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.item;
    }
}
