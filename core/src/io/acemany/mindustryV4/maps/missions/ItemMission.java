package io.acemany.mindustryV4.maps.missions;

import io.acemany.mindustryV4.Vars;
import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.type.Item;
import io.acemany.mindustryV4.world.Tile;
import io.anuke.ucore.util.Bundles;

import static io.acemany.mindustryV4.Vars.state;

/**A mission that is completed when the player obtains items in their core.*/
public class ItemMission extends Mission{
    private final Item item;
    private final int amount;

    public ItemMission(Item item, int amount){
        this.item = item;
        this.amount = amount;
    }

    @Override
    public boolean isComplete(){
        for(Tile tile : state.teams.get(Vars.defaultTeam).cores){
            if(tile.entity.items.has(item, amount)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String displayString(){
        TileEntity core = Vars.players[0].getClosestCore();
        if(core == null) return "imminent doom";
        return Bundles.format("text.mission.resource", item.localizedName(), core.items.get(item), amount);
    }

    @Override
    public String menuDisplayString(){
        return Bundles.format("text.mission.resource.menu", item.localizedName(), amount);
    }
}
