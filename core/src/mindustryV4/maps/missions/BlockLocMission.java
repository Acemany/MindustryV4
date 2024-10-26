package mindustryV4.maps.missions;

import mindustryV4.graphics.Palette;
import mindustryV4.world.Block;
import ucore.core.Timers;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.util.Angles;
import ucore.util.Bundles;
import ucore.util.Mathf;

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

        Draw.color(Palette.accentBack);
        Lines.square(x * tilesize + block.offset(), y * tilesize + block.offset() - 1f, block.size * tilesize/2f + 1f+ Mathf.absin(Timers.time(), 6f, 2f));

        Draw.color(Palette.accent);
        Lines.square(x * tilesize + block.offset(), y * tilesize + block.offset(), block.size * tilesize/2f + 1f+ Mathf.absin(Timers.time(), 6f, 2f));


        if(block.rotate){
            Draw.colorl(0.4f);
            Draw.rect("icon-arrow", x * tilesize + block.offset(), y * tilesize + block.offset() - 1f, rotation*90);
            Draw.colorl(0.6f);
            Draw.rect("icon-arrow", x * tilesize + block.offset(), y * tilesize + block.offset(), rotation*90);
        }

        float rot = players[0].angleTo(x * tilesize + block.offset(), y * tilesize + block.offset());
        float len = 12f;

        Draw.color(Palette.accentBack);
        Draw.rect("icon-arrow", players[0].x + Angles.trnsx(rot, len), players[0].y + Angles.trnsy(rot, len), rot);
        Draw.color(Palette.accent);
        Draw.rect("icon-arrow", players[0].x + Angles.trnsx(rot, len), players[0].y + Angles.trnsy(rot, len) + 1f, rot);

        Draw.reset();
    }

    @Override
    public boolean isComplete(){
        return world.tile(x, y).block() == block && (!block.rotate || world.tile(x,y).getRotation() == rotation);
    }

    @Override
    public String displayString(){
        return Bundles.format("text.mission.block", block.formalName);
    }
}
