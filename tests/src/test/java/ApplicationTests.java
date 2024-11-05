import io.anuke.arc.*;
import io.anuke.arc.backends.headless.HeadlessApplication;
import io.anuke.arc.backends.headless.HeadlessApplicationConfiguration;
import io.anuke.arc.math.geom.Point2;
import mindustryV4.Vars;
import mindustryV4.content.Items;
import mindustryV4.content.UnitTypes;
import mindustryV4.content.Blocks;
import mindustryV4.core.GameState.State;
import mindustryV4.core.Logic;
import mindustryV4.core.NetServer;
import mindustryV4.core.World;
import mindustryV4.entities.type.BaseUnit;
import mindustryV4.game.Content;
import mindustryV4.game.Team;
import mindustryV4.io.BundleLoader;
import mindustryV4.io.SaveIO;
import mindustryV4.maps.*;
import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.Edges;
import mindustryV4.world.Tile;
import io.anuke.arc.util.Time;
import io.anuke.arc.util.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mindustryV4.Vars.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTests{

    @BeforeAll
    static void launchApplication(){
        try{
            boolean[] begins = {false};
            Throwable[] exceptionThrown = {null};
            Log.setUseColors(false);

            ApplicationCore core = new ApplicationCore(){
                @Override
                public void setup(){
                    Vars.init();

                    headless = true;

                    BundleLoader.load();
                    content.load();

                    add(logic = new Logic());
                    add(world = new World());
                    add(netServer = new NetServer());

                    content.initialize(Content::init);
                }

                public void init(){
                    super.init();
                    begins[0] = true;
                }
            };

            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(core, config);

            for(Thread thread : Thread.getAllStackTraces().keySet()){
                if(thread.getName().equals("HeadlessApplication")){
                    thread.setUncaughtExceptionHandler((t, throwable) -> exceptionThrown[0] = throwable);
                    break;
                }
            }

            while(!begins[0]){
                if(exceptionThrown[0] != null){
                    fail(exceptionThrown[0]);
                }
                Thread.sleep(10);
            }
        }catch(Throwable r){
            fail(r);
        }
    }

    @BeforeEach
    void resetWorld(){
        Time.setDeltaProvider(() ->  2f);
        logic.reset();
        state.set(State.menu);
    }

    @Test
    void initialization(){
        assertNotNull(logic);
        assertNotNull(world);
        assertTrue(content.getContentMap().length > 0);
    }

    @Test
    void loadSector(){
        world.sectors.createSector(0, 0);

        Sector first = world.sectors.get(0, 0);
        Log.info(first);

        world.sectors.playSector(first);
    }

    @Test
    void playMap(){
        assertTrue(world.maps.all().size > 0);

        Map first = world.maps.all().first();
        Log.info(first);

        world.loadMap(first);
    }

    @Test
    void spawnWaves(){
        world.loadMap(world.maps.all().first());
        logic.runWave();
        unitGroups[waveTeam.ordinal()].updateEvents();
        assertFalse(unitGroups[waveTeam.ordinal()].isEmpty());
    }

    @Test
    void createMap(){
        assertTrue(world.maps.all().size > 0);

        Tile[][] tiles = world.createTiles(8, 8);

        world.beginMapLoad();
        for(int x = 0; x < tiles.length; x++){
            for(int y = 0; y < tiles[0].length; y++){
                tiles[x][y] = new Tile(x, y, (byte)0, (byte)0);
            }
        }
        world.endMapLoad();
    }

    @Test
    void multiblock(){
        createMap();
        int bx = 4;
        int by = 4;
        world.setBlock(world.tile(bx, by), Blocks.coreShard, Team.blue);
        assertEquals(world.tile(bx, by).getTeam(), Team.blue);
        for(int x = bx-1; x <= bx + 1; x++){
            for(int y = by-1; y <= by + 1; y++){
                if(x == bx && by == y){
                    assertEquals(world.tile(x, y).block(), Blocks.coreShard);
                }else{
                    assertTrue(world.tile(x, y).block() == Blocks.part && world.tile(x, y).getLinked() == world.tile(bx, by));
                }
            }
        }
    }

    @Test
    void blockInventories(){
        multiblock();
        Tile tile = world.tile(4, 4);
        tile.entity.items.add(Items.coal, 5);
        tile.entity.items.add(Items.titanium, 50);
        assertEquals(tile.entity.items.total(), 55);
        tile.entity.items.remove(Items.phaseFabric, 10);
        tile.entity.items.remove(Items.titanium, 10);
        assertEquals(tile.entity.items.total(), 45);
    }

    @Test
    void timers(){
        boolean[] ran = {false};
        Time.run(3.9999f, () -> ran[0] = true);

        Time.update();
        assertFalse(ran[0]);
        Time.update();
        assertTrue(ran[0]);
    }

    @Test
    void save(){
        assertTrue(world.maps.all().size > 0);

        world.loadMap(world.maps.all().first());
        SaveIO.saveToSlot(0);
    }

    @Test
    void load(){
        assertTrue(world.maps.all().size > 0);

        world.loadMap(world.maps.all().first());
        Map map = world.getMap();

        SaveIO.saveToSlot(0);
        resetWorld();
        SaveIO.loadFromSlot(0);

        assertEquals(world.getMap(), map);
        assertEquals(world.width(), map.meta.width);
        assertEquals(world.height(), map.meta.height);
    }

    @Test
    void inventoryDeposit(){
        depositTest(Blocks.smelter, Items.copper);
        depositTest(Blocks.vault, Items.copper);
        depositTest(Blocks.thoriumReactor, Items.thorium);
    }

    @Test
    void edges(){
        Point2[] edges = Edges.getEdges(1);
        assertEquals(edges[0], new Point2(1, 0));
        assertEquals(edges[1], new Point2(0, 1));
        assertEquals(edges[2], new Point2(-1, 0));
        assertEquals(edges[3], new Point2(0, -1));

        Point2[] edges2 = Edges.getEdges(2);
        assertEquals(8, edges2.length);
    }

    void depositTest(Block block, Item item){
        BaseUnit unit = UnitTypes.dagger.create(Team.none);
        Tile tile = new Tile(0, 0, Blocks.air.id, block.id);
        int capacity = tile.block().itemCapacity;

        int deposited = tile.block().acceptStack(item, capacity - 1, tile, unit);
        assertEquals(capacity - 1, deposited);

        tile.block().handleStack(item, capacity - 1, tile, unit);
        assertEquals(tile.entity.items.get(item), capacity - 1);

        int overflow = tile.block().acceptStack(item, 10, tile, unit);
        assertEquals(1, overflow);

        tile.block().handleStack(item, 1, tile, unit);
        assertEquals(capacity, tile.entity.items.get(item));
    }
}
