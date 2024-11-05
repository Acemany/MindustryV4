package mindustryV4.type;

import io.anuke.arc.Core;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.graphics.g2d.TextureRegion;
import io.anuke.arc.scene.ui.layout.Table;
import mindustryV4.entities.type.Player;
import mindustryV4.game.UnlockableContent;
import mindustryV4.graphics.Pal;
import mindustryV4.ui.ContentDisplay;

public class Mech extends UnlockableContent{
    public boolean flying;
    public float speed = 1.1f;
    public float maxSpeed = 10f;
    public float boostSpeed = 0.75f;
    public float drag = 0.4f;
    public float mass = 1f;
    public float shake = 0f;
    public float health = 200f;

    public float hitsize = 6f;
    public float cellTrnsY = 0f;
    public float mineSpeed = 1f;
    public int drillPower = -1;
    public float buildPower = 1f;
    public Color trailColor = Pal.boostFrom;
    public Color trailColorTo = Pal.boostTo;
    public int itemCapacity = 30;
    public boolean turnCursor = true;
    public boolean canHeal = false;

    public float weaponOffsetX, weaponOffsetY, engineOffset = 5f, engineSize = 2.5f;
    public Weapon weapon;

    public TextureRegion baseRegion, legRegion, region, iconRegion;

    public Mech(String name, boolean flying){
        super(name);
        this.flying = flying;
        this.description = Core.bundle.get("mech." + name + ".description");
    }

    public String localizedName(){
        return Core.bundle.get("mech." + name + ".name");
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
    public ContentType getContentType(){
        return ContentType.mech;
    }

    @Override
    public void load(){
        weapon.load();
        if(!flying){
            legRegion = Core.atlas.find(name + "-leg");
            baseRegion = Core.atlas.find(name + "-base");
        }

        region = Core.atlas.find(name);
        iconRegion = Core.atlas.find("mech-icon-" + name);
    }

    @Override
    public String toString(){
        return localizedName();
    }
}