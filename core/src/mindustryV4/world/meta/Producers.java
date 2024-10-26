package mindustryV4.world.meta;

import mindustryV4.game.Content;

public class Producers{
    private Content output;

    public void set(Content content){
        this.output = content;
    }

    public Content get(){
        return output;
    }

    public boolean is(Content content){
        return content == output;
    }
}
