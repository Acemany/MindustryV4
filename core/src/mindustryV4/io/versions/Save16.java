package mindustryV4.io.versions;

import io.anuke.arc.util.Strings;
import io.anuke.arc.util.Time;
import mindustryV4.game.Version;
import mindustryV4.gen.Serialization;
import mindustryV4.io.SaveFileVersion;
import mindustryV4.maps.Map;
import mindustryV4.type.ContentType;

import java.io.*;

import static mindustryV4.Vars.*;

public class Save16 extends SaveFileVersion{

    public Save16(){
        super(16);
    }

    @Override
    public void read(DataInputStream stream) throws IOException{
        stream.readLong(); //time
        stream.readLong(); //total playtime
        stream.readInt(); //build

        //general state
        state.rules = Serialization.readRules(stream);
        String mapname = stream.readUTF();
        Map map = world.maps.getByName(mapname);
        if(map == null) map = new Map(Strings.capitalize(mapname), 1, 1);
        world.setMap(map);

        int wave = stream.readInt();
        float wavetime = stream.readFloat();

        state.wave = wave;
        state.wavetime = wavetime;
        state.stats = Serialization.readStats(stream);
        world.spawner.read(stream);

        content.setTemporaryMapper(readContentHeader(stream));

        readEntities(stream);
        readMap(stream);
    }

    @Override
    public void write(DataOutputStream stream) throws IOException{
        //--META--
        stream.writeInt(version); //version id
        stream.writeLong(Time.millis()); //last saved
        stream.writeLong(headless ? 0 : control.saves.getTotalPlaytime()); //playtime
        stream.writeInt(Version.build); //build

        //--GENERAL STATE--
        Serialization.writeRules(stream, state.rules);
        stream.writeUTF(world.getMap().name); //map name

        stream.writeInt(state.wave); //wave
        stream.writeFloat(state.wavetime); //wave countdown

        Serialization.writeStats(stream, state.stats);
        world.spawner.write(stream);

        writeContentHeader(stream);

        //--ENTITIES--

        writeEntities(stream);

        writeMap(stream);
    }
}