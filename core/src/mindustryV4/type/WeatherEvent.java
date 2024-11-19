package mindustryV4.type;

import mindustryV4.game.Content;

//TODO implement-- should it even be content?
public class WeatherEvent extends Content{
    public final String name;

    public WeatherEvent(String name){
        this.name = name;
    }

    @Override
    public ContentType getContentType(){
        return ContentType.weather;
    }
}