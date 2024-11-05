package mindustryV4.entities.effect;

import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import io.anuke.arc.collection.Array;
import io.anuke.arc.collection.IntSet;
import mindustryV4.entities.EntityGroup;
import mindustryV4.entities.impl.TimedEntity;
import mindustryV4.entities.traits.DrawTrait;
import mindustryV4.entities.traits.TimeTrait;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.graphics.g2d.Draw;
import io.anuke.arc.graphics.g2d.Lines;
import io.anuke.arc.math.Angles;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.math.RandomXS128;
import io.anuke.arc.math.geom.Geometry;
import io.anuke.arc.math.geom.Position;
import io.anuke.arc.math.geom.Rectangle;
import io.anuke.arc.math.geom.Vector2;
import io.anuke.arc.util.pooling.Pools;
import mindustryV4.content.Bullets;
import mindustryV4.entities.type.Unit;
import mindustryV4.entities.Units;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.entities.traits.SyncTrait;
import mindustryV4.game.Team;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Pal;

import java.io.DataInput;
import java.io.DataOutput;

import static mindustryV4.Vars.bulletGroup;

public class Lightning extends TimedEntity implements DrawTrait, SyncTrait, TimeTrait{
    public static final float lifetime = 10f;

    private static final RandomXS128 random = new RandomXS128();
    private static final Rectangle rect = new Rectangle();
    private static final Array<Unit> entities = new Array<>();
    private static final IntSet hit = new IntSet();
    private static final int maxChain = 8;
    private static final float hitRange = 30f;
    private static int lastSeed = 0;

    private Array<Position> lines = new Array<>();
    private Color color = Pal.lancerLaser;

    /**For pooling use only. Do not call directly!*/
    public Lightning(){
    }

    /**Create a lighting branch at a location. Use Team.none to damage everyone.*/
    public static void create(Team team, Color color, float damage, float x, float y, float targetAngle, int length){
        Call.createLighting(lastSeed++, team, color, damage, x, y, targetAngle, length);
    }

    /**Do not invoke!*/
    @Remote(called = Loc.server)
    public static void createLighting(int seed, Team team, Color color, float damage, float x, float y, float rotation, int length){

        Lightning l = Pools.obtain(Lightning.class, Lightning::new);
        Float dmg = damage;

        l.x = x;
        l.y = y;
        l.color = color;
        l.add();

        random.setSeed(seed);
        hit.clear();

        for (int i = 0; i < length/2; i++) {
            Bullet.create(Bullets.damageLightning, l, team, x, y, 0f, 1f, 1f, dmg);
            l.lines.add(new Vector2(x + Mathf.range(3f), y + Mathf.range(3f)));

            rect.setSize(hitRange).setCenter(x, y);
            entities.clear();
            if(hit.size < maxChain){
                Units.getNearbyEnemies(team, rect, u -> {
                    if(!hit.contains(u.getID())){
                        entities.add(u);
                    }
                });
            }

            Unit furthest = Geometry.findFurthest(x, y, entities);

            if(furthest != null){
                hit.add(furthest.getID());
                x = furthest.x;
                y = furthest.y;
            }else{
                rotation += random.range(20f);
                x += Angles.trnsx(rotation, hitRange/2f);
                y += Angles.trnsy(rotation, hitRange/2f);
            }

        }
    }

    @Override
    public boolean isSyncing(){
        return false;
    }

    @Override
    public void write(DataOutput data){}

    @Override
    public void read(DataInput data){}

    @Override
    public float lifetime(){
        return lifetime;
    }

    @Override
    public void reset(){
        super.reset();
        color = Pal.lancerLaser;
        lines.clear();
    }

    @Override
    public void removed(){
        super.removed();
        Pools.free(this);
    }

    @Override
    public void draw(){
        float lx = x, ly = y;
        Draw.color(color, Color.WHITE, fin());
        //TODO this is really, really bad rendering
        for(int i = 0; i < lines.size; i++){
            Position v = lines.get(i);

            float f = (float)i / lines.size;

            Lines.stroke(fout() * 3f * (1.5f - f));

            Lines.stroke(Lines.getStroke() * 4f);
            Draw.alpha(0.3f);
            Lines.line(lx, ly, v.getX(), v.getY());

            Lines.stroke(Lines.getStroke() / 4f);
            Draw.alpha(1f);
            Lines.line(lx, ly, v.getX(), v.getY());

            Lines.stroke(3f * fout() * (1f - f));

            lx = v.getX();
            ly = v.getY();
        }
        Draw.color();
    }

    @Override
    public EntityGroup targetGroup(){
        return bulletGroup;
    }
}
