package mindustryV4.io;

import mindustryV4.game.Rules;
import mindustryV4.maps.Map;

import static mindustryV4.Vars.world;

public class SaveMeta{
    public int version;
    public int build;
    public long timestamp;
    public long timePlayed;
    public Map map;
    public int wave;
    public Rules rules;

    public SaveMeta(int version, long timestamp, long timePlayed, int build, String map, int wave, Rules rules){
        this.version = version;
        this.build = build;
        this.timestamp = timestamp;
        this.timePlayed = timePlayed;
        this.map = world.maps.getByName(map);
        this.wave = wave;
        this.rules = rules;
    }
}
