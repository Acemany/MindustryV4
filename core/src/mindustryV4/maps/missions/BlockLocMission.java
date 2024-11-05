package mindustryV4.maps.missions;

import io.anuke.arc.*;
import mindustryV4.graphics.Pal;
import mindustryV4.world.Block;
import io.anuke.arc.util.Time;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.Lines;
import io.anuke.arc.math.Angles;
import io.anuke.arc.math.Mathf;

import static mindustryV4.Vars.players;
import static mindustryV4.Vars.tilesize;
import static mindustryV4.Vars.world;

public class BlockLocMission extends Mission{
    private final Block block;
    private final int x, y, rotation;

    public BlockLocMission(Block block, int x, int y, int rotation){
        this.block = block;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public BlockLocMission(Block block, int x, int y){
        this.block = block;
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }

    @Override
    public void drawOverlay(){
        Lines.stroke(2f);

        Draw.color(Pal.accentBack);
        Lines.square(x * tilesize + block.offset(), y * tilesize + block.offset() - 1f, block.size * tilesize/2f + 1f+ Mathf.absin(Time.time(), 6f, 2f));

        Draw.color(Pal.accent);
        Lines.square(x * tilesize + block.offset(), y * tilesize + block.offset(), block.size * tilesize/2f + 1f+ Mathf.absin(Time.time(), 6f, 2f));


        if(block.rotate){
            Draw.colorl(0.4f);
            Draw.rect("icon-arrow", x * tilesize + block.offset(), y * tilesize + block.offset() - 1f, rotation*90);
            Draw.colorl(0.6f);
            Draw.rect("icon-arrow", x * tilesize + block.offset(), y * tilesize + block.offset(), rotation*90);
        }

        float rot = players[0].angleTo(x * tilesize + block.offset(), y * tilesize + block.offset());
        float len = 12f;

        Draw.color(Pal.accentBack);
        Draw.rect("icon-arrow", players[0].x + Angles.trnsx(rot, len), players[0].y + Angles.trnsy(rot, len), rot);
        Draw.color(Pal.accent);
        Draw.rect("icon-arrow", players[0].x + Angles.trnsx(rot, len), players[0].y + Angles.trnsy(rot, len) + 1f, rot);

        Draw.reset();
    }

    @Override
    public boolean isComplete(){
        return world.tile(x, y).block() == block && (!block.rotate || world.tile(x,y).getRotation() == rotation);
    }

    @Override
    public String displayString(){
        return Core.bundle.format("mission.block", block.localizedName());
    }
}
