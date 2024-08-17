package io.acemany.mindustryV4.type;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.acemany.mindustryV4.content.Weapons;
import io.acemany.mindustryV4.entities.Player;
import io.acemany.mindustryV4.game.UnlockableContent;
import io.acemany.mindustryV4.graphics.Palette;
import io.acemany.mindustryV4.ui.ContentDisplay;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.Bundles;

public class Mech extends UnlockableContent{
    public final String name;
    public final String description;

    public boolean flying;
    public float speed = 1.1f;
    public float maxSpeed = 10f;
    public float boostSpeed = 0.75f;
    public float drag = 0.4f;
    public float mass = 1f;
    public float shake = 0f;
    public float armor = 1f;

    public float hitsize = 6f;
    public float cellTrnsY = 0f;
    public float mineSpeed = 1f;
    public int drillPower = -1;
    public float carryWeight = 10f;
    public float buildPower = 1f;
    public Color trailColor = Palette.boostFrom;
    public Color trailColorTo = Palette.boostTo;
    public int itemCapacity = 30;
    public boolean turnCursor = true;

    public float weaponOffsetX, weaponOffsetY;
    public Weapon weapon = Weapons.blaster;

    public TextureRegion baseRegion, legRegion, region, iconRegion;

    public Mech(String name, boolean flying){
        this.flying = flying;
        this.name = name;
        this.description = Bundles.get("mech." + name + ".description");
    }

    public String localizedName(){
        return Bundles.get("mech." + name + ".name");
    }

    public void updateAlt(Player player){}

    public void draw(Player player){}

    public float getExtraArmor(Player player){
        return 0f;
    }

    public float spreadX(Player player){
        return 0f;
    }

    public float getRotationAlpha(Player player){return 1f;}

    public boolean canShoot(Player player){
        return true;
    }

    public void onLand(Player player){}

    @Override
    public void displayInfo(Table table){
        ContentDisplay.displayMech(table, this);
    }

    @Override
    public TextureRegion getContentIcon(){
        return iconRegion;
    }

    @Override
    public String getContentName(){
        return name;
    }

    @Override
    public ContentType getContentType(){
        return ContentType.mech;
    }

    @Override
    public void load(){
        if(!flying){
            legRegion = Draw.region(name + "-leg");
            baseRegion = Draw.region(name + "-base");
        }

        region = Draw.region(name);
        iconRegion = Draw.region("mech-icon-" + name);
    }

    @Override
    public String toString(){
        return localizedName();
    }
}
