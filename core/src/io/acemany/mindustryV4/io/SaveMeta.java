package io.acemany.mindustryV4.io;

import io.acemany.mindustryV4.game.Difficulty;
import io.acemany.mindustryV4.game.GameMode;
import io.acemany.mindustryV4.maps.Map;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.acemany.mindustryV4.Vars.world;

public class SaveMeta{
    public int version;
    public int build;
    public String date;
    public long timePlayed;
    public int sector;
    public GameMode mode;
    public Map map;
    public int wave;
    public Difficulty difficulty;

    public SaveMeta(int version, long date, long timePlayed, int build, int sector, int mode, String map, int wave, Difficulty difficulty){
        this.version = version;
        this.build = build;
        this.date = SimpleDateFormat.getDateTimeInstance().format(new Date(date));
        this.timePlayed = timePlayed;
        this.sector = sector;
        this.mode = GameMode.values()[mode];
        this.map = world.maps.getByName(map);
        this.wave = wave;
        this.difficulty = difficulty;
    }
}
