package io.acemany.mindustryV4.entities.traits;

import io.acemany.mindustryV4.entities.Unit;
import io.acemany.mindustryV4.world.Tile;

public interface SpawnerTrait{
    Tile getTile();

    void updateSpawning(Unit unit);

    float getSpawnProgress();
}
