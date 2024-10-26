package mindustryV4.game;

import mindustryV4.type.ContentType;

/**Interface for a list of content to be loaded in {@link mindustryV4.core.ContentLoader}.*/
public interface ContentList{
    /**This method should create all the content.*/
    void load();

    /**This method should return the type of content being loaded.*/
    ContentType type();
}
