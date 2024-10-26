package mindustryV4.entities.traits;

import mindustryV4.entities.Unit;
import mindustryV4.world.Tile;

public interface SpawnerTrait{
    Tile getTile();

    void updateSpawning(Unit unit);

    float getSpawnProgress();
}
