package mindustryV4.content.bullets;

import mindustryV4.game.ContentList;
import mindustryV4.type.ContentType;

public abstract class BulletList implements ContentList{

    @Override
    public ContentType type(){
        return ContentType.bullet;
    }
}
