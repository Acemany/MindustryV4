package io.acemany.mindustryV4.content.blocks;

import io.acemany.mindustryV4.game.ContentList;
import io.acemany.mindustryV4.type.ContentType;

public abstract class BlockList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.item;
    }
}
