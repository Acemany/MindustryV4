package io.acemany.mindustryV4.game;

import io.acemany.mindustryV4.type.ContentType;

/**Interface for a list of content to be loaded in {@link io.acemany.mindustryV4.core.ContentLoader}.*/
public interface ContentList{
    /**This method should create all the content.*/
    void load();

    /**This method should return the type of content being loaded.*/
    ContentType type();
}
