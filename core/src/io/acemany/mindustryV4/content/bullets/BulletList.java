package io.acemany.mindustryV4.content.bullets;

import io.acemany.mindustryV4.game.ContentList;
import io.acemany.mindustryV4.type.ContentType;

public abstract class BulletList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.bullet;
    }
}
