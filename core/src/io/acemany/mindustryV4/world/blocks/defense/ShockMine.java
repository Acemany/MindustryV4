package io.acemany.mindustryV4.world.blocks.defense;

import io.acemany.mindustryV4.entities.Unit;
import io.acemany.mindustryV4.entities.effect.Lightning;
import io.acemany.mindustryV4.graphics.Layer;
import io.acemany.mindustryV4.graphics.Palette;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.Tile;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.graphics.Fill;
import io.anuke.ucore.util.Mathf;

public class ShockMine extends Block{
    protected int timerDamage = timers ++;

    protected float cooldown = 80f;
    protected float tileDamage = 5f;
    protected float damage = 13;
    protected int length = 10;
    protected int tendrils = 6;

    public ShockMine(String name){
        super(name);
        update = false;
        destructible = true;
        solid = false;
        shadow = "shadow-shock-mine";
        targetable = false;
        layer = Layer.overlay;
    }

    @Override
    public void drawLayer(Tile tile){
        super.draw(tile);
        Draw.color(tile.getTeam().color);
        Draw.alpha(0.22f);
        Fill.rect(tile.drawx(), tile.drawy(), 2f, 2f);
        Draw.color();
    }

    @Override
    public void draw(Tile tile){
        //nope
    }

    @Override
    public void unitOn(Tile tile, Unit unit){
        if(unit.getTeam() != tile.getTeam() && tile.entity.timer.get(timerDamage, cooldown)){
            for(int i = 0; i < tendrils; i++){
                Lightning.create(tile.getTeam(), Palette.lancerLaser, damage, tile.drawx(), tile.drawy(), Mathf.random(360f), length);
            }
            tile.entity.damage(tileDamage);
        }
    }
}
