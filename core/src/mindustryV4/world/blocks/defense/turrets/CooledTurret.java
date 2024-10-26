package mindustryV4.world.blocks.defense.turrets;

import mindustryV4.content.fx.BlockFx;
import mindustryV4.type.Liquid;
import mindustryV4.world.Tile;
import mindustryV4.world.consumers.ConsumeLiquidFilter;
import ucore.core.Effects;
import ucore.core.Effects.Effect;
import ucore.core.Timers;
import ucore.util.Mathf;

import static mindustryV4.Vars.tilesize;

public class CooledTurret extends Turret{
    /**How much reload is lowered by for each unit of liquid of heat capacity 1.*/
    protected float coolantMultiplier = 1f;
    /**Max coolant used per tick.*/
    protected float maxCoolantUsed = 1f;
    protected Effect coolEffect = BlockFx.fuelburn;

    public CooledTurret(String name){
        super(name);
        hasLiquids = true;
        liquidCapacity = 20f;

        consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.01f)).update(false).optional(true);
    }

    @Override
    protected void updateShooting(Tile tile){
        super.updateShooting(tile);

        TurretEntity entity = tile.entity();
        Liquid liquid = entity.liquids.current();

        float used = Math.min(Math.min(entity.liquids.get(liquid), maxCoolantUsed * Timers.delta()), Math.max(0, ((reload - entity.reload) / coolantMultiplier) / liquid.heatCapacity));
        entity.reload += (used * liquid.heatCapacity) / liquid.heatCapacity;
        entity.liquids.remove(liquid, used);

        if(Mathf.chance(0.06 * used)){
            Effects.effect(coolEffect, tile.drawx() + Mathf.range(size * tilesize / 2f), tile.drawy() + Mathf.range(size * tilesize / 2f));
        }
    }

    @Override
    public boolean acceptLiquid(Tile tile, Tile source, Liquid liquid, float amount){
        return super.acceptLiquid(tile, source, liquid, amount) && liquid.temperature <= 0.5f;
    }
}
