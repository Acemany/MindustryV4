package mindustryV4.graphics;

import io.anuke.arc.Core;
import io.anuke.arc.Events;
import io.anuke.arc.collection.Array;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.graphics.GL20;
import io.anuke.arc.graphics.Pixmap.Format;
import io.anuke.arc.graphics.Texture;
import io.anuke.arc.graphics.g2d.TextureAtlas.TextureAtlasData.*;
import io.anuke.arc.graphics.g2d.TextureRegion;
import io.anuke.arc.graphics.glutils.FrameBuffer;
import io.anuke.arc.util.Disposable;
import mindustryV4.entities.type.Unit;
import mindustryV4.game.EventType.TileChangeEvent;
import mindustryV4.game.EventType.WorldLoadEvent;
import mindustryV4.world.Tile;
import mindustryV4.entities.EntityDraw;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.Fill;

import java.nio.ByteBuffer;

import static mindustryV4.Vars.*;

//TODO bring fog of war back

/**Used for rendering fog of war. A framebuffer is used for this.*/
public class FogRenderer implements Disposable{
    private TextureRegion region = new TextureRegion();
    private FrameBuffer buffer;
    private ByteBuffer pixelBuffer;
    private Array<Tile> changeQueue = new Array<>();
    private int shadowPadding;
    private boolean dirty;

    public FogRenderer(){
        Events.on(WorldLoadEvent.class, event -> {
            dispose();

            shadowPadding = -1;

            buffer = new FrameBuffer(Format.RGBA8888, world.width(), world.height(), false);
            changeQueue.clear();

            //clear buffer to black
            buffer.begin();
            Core.graphics.clear(0, 0, 0, 1f);
            buffer.end();

            for(int x = 0; x < world.width(); x++){
                for(int y = 0; y < world.height(); y++){
                    Tile tile = world.tile(x, y);
                    if(tile.getTeam() == players[0].getTeam() && tile.block().synthetic() && tile.block().viewRange > 0){
                        changeQueue.add(tile);
                    }
                }
            }

            pixelBuffer = ByteBuffer.allocateDirect(world.width() * world.height() * 4);
            dirty = true;
        });

        Events.on(TileChangeEvent.class, event -> {
            if(event.tile.getTeam() == players[0].getTeam() && event.tile.block().synthetic() && event.tile.block().viewRange > 0){
                changeQueue.add(event.tile);
            }
        });
    }

    public void writeFog(){
        if(buffer == null) return;

        buffer.begin();
        pixelBuffer.position(0);
        Core.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);
        Core.gl.glReadPixels(0, 0, world.width(), world.height(), GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixelBuffer);

        pixelBuffer.position(0);
        for(int i = 0; i < world.width() * world.height(); i++){
            byte r = pixelBuffer.get();
            if(r != 0){
                world.tile(i).setVisibility((byte)1);
            }
            pixelBuffer.position(pixelBuffer.position() + 3);
        }
        buffer.end();
    }

    public int getPadding(){
        return -shadowPadding;
    }

    public void draw(){
        if(buffer == null) return;

        float vw = Core.camera.width;
        float vh = Core.camera.height;

        float px = Core.camera.position.x - vw / 2f;
        float py = Core.camera.position.y - vh / 2f;

        float u = (px / tilesize) / buffer.getWidth();
        float v = (py / tilesize) / buffer.getHeight();

        float u2 = ((px + vw) / tilesize) / buffer.getWidth();
        float v2 = ((py + vh) / tilesize) / buffer.getHeight();

        //Core.batch.getProjectionMatrix().setToOrtho2D(0, 0, buffer.getWidth() * tilesize, buffer.getHeight() * tilesize);

        Draw.color(Color.WHITE);

        buffer.begin();

        //Graphics.beginClip((-shadowPadding), (-shadowPadding), (world.width() + shadowPadding*2), (world.height() + shadowPadding*2));

        EntityDraw.setClip(false);

        renderer.drawAndInterpolate(playerGroup, player -> !player.isDead() && player.getTeam() == players[0].getTeam(), Unit::drawView);
        renderer.drawAndInterpolate(unitGroups[players[0].getTeam().ordinal()], unit -> !unit.isDead(), Unit::drawView);

        for(Tile tile : changeQueue){
            float viewRange = tile.block().viewRange;
            if(viewRange < 0) continue;
            Fill.circle(tile.drawx(), tile.drawy(), tile.block().viewRange);
        }

        changeQueue.clear();

        if(dirty){
            for(int i = 0; i < world.width() * world.height(); i++){
                Tile tile = world.tile(i);
                if(tile.discovered()){
                    Fill.rect(tile.worldx(), tile.worldy(), tilesize, tilesize);
                }
            }
            dirty = false;
        }

        EntityDraw.setClip(true);
        buffer.end();


        region.setTexture(buffer.getTexture());
        region.set(u, v2, u2, v);

        //Core.batch.setProjection(Core.camera.combined);
        Draw.shader(Shaders.fog);
        //renderer.pixelSurface.getBuffer().begin();

        Draw.rect(region, px, py, vw, vh);

        //renderer.pixelSurface.getBuffer().end();
        Draw.shader();

        //Graphics.setScreen();
        //Draw.rect(renderer.pixelSurface.texture(), 0, Core.graphics.getHeight(), Core.graphics.getWidth(), -Core.graphics.getHeight());
        Draw.flush();
    }

    public Texture getTexture(){
        return buffer.getTexture();
    }

    @Override
    public void dispose(){
        if(buffer != null) buffer.dispose();
    }
}
