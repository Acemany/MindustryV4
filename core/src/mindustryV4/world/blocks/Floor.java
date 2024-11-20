package mindustryV4.world.blocks;

import io.anuke.arc.Core;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.TextureRegion;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.math.geom.*;
import io.anuke.arc.function.*;
import mindustryV4.content.Fx;
import mindustryV4.content.StatusEffects;
import mindustryV4.entities.Effects.Effect;
import mindustryV4.type.Item;
import mindustryV4.type.Liquid;
import mindustryV4.type.StatusEffect;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;

import static mindustryV4.Vars.tilesize;

public class Floor extends Block{
    /** number of different variant regions to use */
    public int variants;
    /** edge fallback, used mainly for ores */
    public String edge = "stone";
    /** Multiplies unit velocity by this when walked on. */
    public float speedMultiplier = 1f;
    /** Multiplies unit drag by this when walked on. */
    public float dragMultiplier = 1f;
    /** Damage taken per tick on this tile. */
    public float damageTaken = 0f;
    /** How many ticks it takes to drown on this. */
    public float drownTime = 0f;
    /** Effect when walking on this floor. */
    public Effect walkEffect = Fx.ripple;
    /** Effect displayed when drowning on this floor. */
    public Effect drownUpdateEffect = Fx.bubble;
    /** Status effect applied when walking on. */
    public StatusEffect status = StatusEffects.none;
    /** Intensity of applied status effect. */
    public float statusDuration = 60f;
    /** Color of this floor's liquid. Used for tinting sprites. */
    public Color liquidColor;
    /** liquids that drop from this block, used for pumps */
    public Liquid liquidDrop = null;
    /** item that drops from this block, used for drills */
    public Item itemDrop = null;
    /** Whether ores generate on this block. */
    public boolean hasOres = false;
    /** whether this block can be drowned in */
    public boolean isLiquid;
    /** Heat of this block, 0 at baseline. Used for calculating output of thermal generators.*/
    public float heat = 0f;
    /** if true, this block cannot be mined by players. useful for annoying things like sand or stone. */
    public boolean playerUnmineable = false;
    /**Style of the edge stencil. Loaded by looking up "edge-stencil-{name}".*/
    public String edgeStyle = "smooth";
    /**Group of blocks that this block does not draw edges on.*/
    public Block blendGroup = this;
    /**Effect displayed when randomly updated.*/
    public Effect updateEffect = Fx.none;

    protected TextureRegion edgeRegion;
    protected TextureRegion[] edgeRegions;
    protected TextureRegion[] cliffRegions;
    protected TextureRegion[] variantRegions;
    protected Vector2[] offsets;
    protected Predicate<Floor> blends = block -> block != this && !block.blendOverride(this);
    protected BiPredicate<Tile, Tile> tileBlends = (tile, other) -> false;
    protected boolean blend = true;

    public Floor(String name){
        super(name);
        variants = 3;
    }

    @Override
    public void load(){
        super.load();

        if(blend){
            edgeRegion = Core.atlas.has(name + "edge") ? Core.atlas.find(name + "edge") : Core.atlas.find(edge + "edge");
            edgeRegions = new TextureRegion[8];
            offsets = new Vector2[8];

            for(int i = 0; i < 8; i++){
                int dx = Geometry.d8[i].x, dy = Geometry.d8[i].y;

                TextureRegion result = new TextureRegion();

                int sx = -dx * 8 + 2, sy = -dy * 8 + 2;
                int x = Mathf.clamp(sx, 0, 12);
                int y = Mathf.clamp(sy, 0, 12);
                int w = Mathf.clamp(sx + 8, 0, 12) - x, h = Mathf.clamp(sy + 8, 0, 12) - y;

                float rx = Mathf.clamp(dx * 8, 0, 8 - w);
                float ry = Mathf.clamp(dy * 8, 0, 8 - h);

                result.setTexture(edgeRegion.getTexture());
                result.set(edgeRegion.getX() + x, edgeRegion.getY() + y + h, w, -h);

                edgeRegions[i] = result;
                offsets[i] = new Vector2(-4 + rx, -4 + ry);
            }

            cliffRegions = new TextureRegion[4];
            cliffRegions[0] = Core.atlas.find(name + "-cliff-edge-2");
            cliffRegions[1] = Core.atlas.find(name + "-cliff-edge");
            cliffRegions[2] = Core.atlas.find(name + "-cliff-edge-1");
            cliffRegions[3] = Core.atlas.find(name + "-cliff-side");
        }

        //load variant regions for drawing
        if(variants > 0){
            variantRegions = new TextureRegion[variants];

            for(int i = 0; i < variants; i++){
                variantRegions[i] = Core.atlas.find(name + (i + 1));
            }
        }else{
            variantRegions = new TextureRegion[1];
            variantRegions[0] = Core.atlas.find(name);
        }
    }

    @Override
    public TextureRegion[] generateIcons(){
        return new TextureRegion[]{Core.atlas.find(Core.atlas.has(name) ? name : name + "1")};
    }

    @Override
    public void init(){
        super.init();

        if(isLiquid && liquidColor == null){
            throw new RuntimeException("All liquids must define a liquidColor! Problematic block: " + name);
        }
    }

    @Override
    public void draw(Tile tile){
        Mathf.random.setSeed(tile.pos());

        Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx(), tile.worldy());

        if(tile.hasCliffs() && cliffRegions != null){
            for(int i = 0; i < 4; i++){
                if((tile.getCliffs() & (1 << i * 2)) != 0){
                    Draw.colorl(i > 1 ? 0.6f : 1f);

                    boolean above = (tile.getCliffs() & (1 << ((i + 1) % 4) * 2)) != 0, below = (tile.getCliffs() & (1 << (Mathf.mod(i - 1, 4)) * 2)) != 0;

                    if(above && below){
                        Draw.rect(cliffRegions[0], tile.worldx(), tile.worldy(), i * 90);
                    }else if(above){
                        Draw.rect(cliffRegions[1], tile.worldx(), tile.worldy(), i * 90);
                    }else if(below){
                        Draw.rect(cliffRegions[2], tile.worldx(), tile.worldy(), i * 90);
                    }else{
                        Draw.rect(cliffRegions[3], tile.worldx(), tile.worldy(), i * 90);
                    }
                }
            }
        }
        Draw.reset();

        drawEdges(tile);
    }

    public boolean blendOverride(Block block){
        return false;
    }

    protected void drawEdges(Tile tile){
        if(!blend || tile.getCliffs() > 0) return;

        for(int i = 0; i < 8; i++){
            int dx = Geometry.d8[i].x, dy = Geometry.d8[i].y;

            Tile other = tile.getNearby(dx, dy);

            if(other == null) continue;

            Floor floor = other.floor();

            if(floor.edgeRegions == null || (floor.id <= this.id && !(tile.getElevation() != -1 && other.getElevation() > tile.getElevation())) || (!blends.test(floor) && !tileBlends.test(tile, other)) || (floor.cacheLayer.ordinal() > this.cacheLayer.ordinal())) continue;

            TextureRegion region = floor.edgeRegions[i];

            Draw.rect(region, tile.worldx() + floor.offsets[i].x, tile.worldy() + floor.offsets[i].y, region.getWidth(), region.getHeight());
        }
    }
}
