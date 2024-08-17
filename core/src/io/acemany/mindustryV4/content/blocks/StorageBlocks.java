package io.acemany.mindustryV4.content.blocks;

import io.acemany.mindustryV4.game.ContentList;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.blocks.storage.CoreBlock;
import io.acemany.mindustryV4.world.blocks.storage.SortedUnloader;
import io.acemany.mindustryV4.world.blocks.storage.Vault;

public class StorageBlocks extends BlockList implements ContentList{
    public static Block core, vault, container, unloader;

    @Override
    public void load(){
        core = new CoreBlock("core"){{
            health = 1100;
        }};

        vault = new Vault("vault"){{
            size = 3;
            itemCapacity = 900;
        }};

        container = new Vault("container"){{
            size = 2;
            itemCapacity = 200;
        }};

        unloader = new SortedUnloader("unloader"){{
            speed = 12f;
        }};
    }
}
