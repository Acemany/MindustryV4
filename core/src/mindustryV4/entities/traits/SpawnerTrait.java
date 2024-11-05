package mindustryV4.entities.traits;

import io.anuke.arc.math.geom.Position;
import mindustryV4.entities.type.Player;
import mindustryV4.world.Tile;

public interface SpawnerTrait extends TargetTrait, Position{
    Tile getTile();

    void updateSpawning(Player unit);

    //float getSpawnProgress();

    @Override
    default boolean isValid(){
        return getTile().entity instanceof SpawnerTrait;
    }
}
