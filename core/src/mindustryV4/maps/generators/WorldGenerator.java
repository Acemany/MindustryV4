package mindustryV4.maps.generators;

import io.anuke.arc.math.geom.Point2;
import io.anuke.arc.collection.Array;
import io.anuke.arc.collection.IntArray;
import io.anuke.arc.collection.ObjectMap;
import mindustryV4.content.Items;
import mindustryV4.content.Blocks;
import mindustryV4.game.Team;
import mindustryV4.maps.Map;
import mindustryV4.maps.MapTileData;
import mindustryV4.maps.MapTileData.TileDataMarker;
import mindustryV4.maps.Sector;
import mindustryV4.maps.missions.Mission;
import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.Floor;
import mindustryV4.world.blocks.OreBlock;
import io.anuke.arc.util.noise.RidgedPerlin;
import io.anuke.arc.util.noise.Simplex;
import io.anuke.arc.math.geom.Geometry;
import io.anuke.arc.math.Mathf;
import io.anuke.arc.math.RandomXS128;
import io.anuke.arc.util.Structs;

import static mindustryV4.Vars.*;


public class WorldGenerator{
    private static final int baseSeed = 0;
    private int oreIndex = 0;

    private Simplex sim = new Simplex(baseSeed);
    private Simplex sim2 = new Simplex(baseSeed + 1);
    private Simplex sim3 = new Simplex(baseSeed + 2);
    private RidgedPerlin rid = new RidgedPerlin(baseSeed + 4, 1);
    private RandomXS128 random = new RandomXS128(baseSeed + 3);

    private GenResult result = new GenResult();
    private ObjectMap<Block, Block> decoration;

    public WorldGenerator(){
        decoration = ObjectMap.of(
            Blocks.grass, Blocks.shrub,
            Blocks.stone, Blocks.rock,
            Blocks.ice, Blocks.iceRock,
            Blocks.snow, Blocks.iceRock,
            Blocks.blackStone, Blocks.blackrock
        );
    }

    /**Loads raw map tile data into a Tile[][] array, setting up multiblocks, cliffs and ores. */
    public void loadTileData(Tile[][] tiles, MapTileData data, boolean genOres, int seed){
        data.position(0, 0);
        TileDataMarker marker = data.newDataMarker();

        for(int y = 0; y < data.height(); y++){
            for(int x = 0; x < data.width(); x++){
                data.read(marker);

                tiles[x][y] = new Tile(x, y, marker.floor, marker.wall == Blocks.part.id ? 0 : marker.wall, marker.rotation, marker.team, marker.elevation);
            }
        }

        prepareTiles(tiles);

        generateOres(tiles, seed, genOres, null);
    }

    /**'Prepares' a tile array by:<br>
    * - setting up multiblocks<br>
    * - updating cliff data<br>
    * - removing ores on cliffs<br>
    * Usually used before placing structures on a tile array.*/
    public void prepareTiles(Tile[][] tiles){

        //find multiblocks
        IntArray multiblocks = new IntArray();

        for(int x = 0; x < tiles.length; x++){
            for(int y = 0; y < tiles[0].length; y++){
                Tile tile = tiles[x][y];

                if(tile.block().isMultiblock()){
                    multiblocks.add(tile.pos());
                }
            }
        }

        //place multiblocks now
        for(int i = 0; i < multiblocks.size; i++){
            int pos = multiblocks.get(i);

            int x = pos % tiles.length;
            int y = pos / tiles.length;

            Block result = tiles[x][y].block();
            Team team = tiles[x][y].getTeam();

            int offsetx = -(result.size - 1) / 2;
            int offsety = -(result.size - 1) / 2;

            for(int dx = 0; dx < result.size; dx++){
                for(int dy = 0; dy < result.size; dy++){
                    int worldx = dx + offsetx + x;
                    int worldy = dy + offsety + y;
                    if(!(worldx == x && worldy == y)){
                        Tile toplace = world.tile(worldx, worldy);
                        if(toplace != null){
                            toplace.setLinked((byte) (dx + offsetx), (byte) (dy + offsety));
                            toplace.setTeam(team);
                        }
                    }
                }
            }
        }

        //update cliffs, occlusion data
        for(int x = 0; x < tiles.length; x++){
            for(int y = 0; y < tiles[0].length; y++){
                Tile tile = tiles[x][y];

                tile.updateOcclusion();

                //fix things on cliffs that shouldn't be
                if(tile.block() != Blocks.air && tile.hasCliffs() && !tile.block().isMultiblock() && tile.block() != Blocks.part){
                    tile.setBlock(Blocks.air);
                }

                //remove ore veins on cliffs
                if(tile.floor() instanceof OreBlock && tile.hasCliffs()){
                    tile.setFloor(((OreBlock)tile.floor()).base);
                }
            }
        }
    }

    public void playRandomMap(){
        ui.loadAnd(() -> {
            world.setSector(null);
            logic.reset();

            int sx = (short)Mathf.range(Short.MAX_VALUE/2);
            int sy = (short)Mathf.range(Short.MAX_VALUE/2);
            int width = 380;
            int height = 380;
            Array<Point2> spawns = new Array<>();
            Array<Item> ores = Item.getAllOres();

            if(state.rules.pvp){
                int scaling = 10;
                spawns.add(new Point2(width/scaling, height/scaling));
                spawns.add(new Point2(width - 1 - width/scaling, height - 1 - height/scaling));
            }else{
                spawns.add(new Point2(width/2, height/2));
            }

            Tile[][] tiles = world.createTiles(width, height);
            world.setMap(new Map("Generated Map", width, height));
            world.beginMapLoad();

            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    generateTile(result, sx, sy, x, y, true, spawns, ores);
                    tiles[x][y] = new Tile(x, y, result.floor.id, result.wall.id, (byte)0, (byte)0, result.elevation);
                }
            }

            prepareTiles(tiles);

            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    Tile tile = tiles[x][y];

                    byte elevation = tile.getElevation();

                    for(Point2 point : Geometry.d4){
                        if(!Structs.inBounds(x + point.x, y + point.y, width, height)) continue;
                        if(tiles[x + point.x][y + point.y].getElevation() < elevation){

                            if(sim2.octaveNoise2D(1, 1, 1.0 / 8, x, y) > 0.8){
                                tile.setElevation(-1);
                            }
                            break;
                        }
                    }
                }
            }

            world.setBlock(tiles[spawns.get(0).x][spawns.get(0).y], Blocks.coreShard, Team.blue);

            if(state.rules.pvp){
                world.setBlock(tiles[spawns.get(1).x][spawns.get(1).y], Blocks.coreShard, Team.red);
            }

            world.endMapLoad();
            logic.play();
        });
    }

    public void generateOres(Tile[][] tiles, long seed, boolean genOres, Array<Item> usedOres){
        oreIndex = 0;

        if(genOres){
            Array<OreEntry> baseOres = Array.with(
                new OreEntry(Items.copper, 0.3f, seed),
                new OreEntry(Items.coal, 0.284f, seed),
                new OreEntry(Items.lead, 0.28f, seed),
                new OreEntry(Items.titanium, 0.27f, seed),
                new OreEntry(Items.thorium, 0.26f, seed)
            );

            Array<OreEntry> ores = new Array<>();
            if(usedOres == null){
                ores.addAll(baseOres);
            }else{
                for(Item item : usedOres){
                    ores.add(baseOres.select(entry -> entry.item == item).iterator().next());
                }
            }

            for(int x = 0; x < tiles.length; x++){
                for(int y = 0; y < tiles[0].length; y++){

                    Tile tile = tiles[x][y];

                    if(!tile.floor().hasOres || tile.hasCliffs() || tile.block() != Blocks.air){
                        continue;
                    }

                    for(int i = ores.size - 1; i >= 0; i--){
                        OreEntry entry = ores.get(i);
                        if(entry.noise.octaveNoise2D(1, 0.7, 1f / (4 + i * 2), x, y) / 4f +
                        Math.abs(0.5f - entry.noise.octaveNoise2D(2, 0.7, 1f / (50 + i * 2), x, y)) > 0.48f &&
                        Math.abs(0.5f - entry.noise.octaveNoise2D(1, 1, 1f / (55 + i * 4), x, y)) > 0.22f){
                            tile.setFloor(OreBlock.get(tile.floor(), entry.item));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void generateMap(Tile[][] tiles, Sector sector){
        int width = tiles.length, height = tiles[0].length;
        RandomXS128 rnd = new RandomXS128(sector.getSeed());
        Generation gena = new Generation(sector, tiles, tiles.length, tiles[0].length, rnd);
        Array<Point2> spawnpoints = sector.currentMission().getSpawnPoints(gena);
        Array<Item> ores = world.sectors.getOres(sector.x, sector.y);

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                GenResult result = generateTile(this.result, sector.x, sector.y, x, y, true, spawnpoints, ores);
                Tile tile = new Tile(x, y, result.floor.id, result.wall.id, (byte)0, (byte)0, result.elevation);
                tiles[x][y] = tile;
            }
        }

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Tile tile = tiles[x][y];

                byte elevation = tile.getElevation();

                for(Point2 point : Geometry.d4){
                    if(!Structs.inBounds(x + point.x, y + point.y, width, height)) continue;
                    if(tiles[x + point.x][y + point.y].getElevation() < elevation){

                        if(sim2.octaveNoise2D(1, 1, 1.0 / 8, x, y) > 0.8){
                            tile.setElevation(-1);
                        }
                        break;
                    }
                }
            }
        }

        for(int x = 0; x < tiles.length; x++){
            for(int y = 0; y < tiles[0].length; y++){
                Tile tile = tiles[x][y];

                tile.updateOcclusion();
            }
        }

        Generation gen = new Generation(sector, tiles, tiles.length, tiles[0].length, random);

        for(Mission mission : sector.missions){
            mission.generate(gen);
        }

        prepareTiles(tiles);
    }

    public GenResult generateTile(int sectorX, int sectorY, int localX, int localY){
        return generateTile(sectorX, sectorY, localX, localY, true);
    }

    public GenResult generateTile(int sectorX, int sectorY, int localX, int localY, boolean detailed){
        return generateTile(result, sectorX, sectorY, localX, localY, detailed, null, null);
    }

    /**
     * Gets the generation result from a specific sector at specific coordinates.
     * @param result where to put the generation results
     * @param sectorX X of the sector in terms of sector coordinates
     * @param sectorY Y of the sector in terms of sector coordinates
     * @param localX X in terms of local sector tile coordinates
     * @param localY Y in terms of local sector tile coordinates
     * @param detailed whether the tile result is 'detailed' (e.g. previews should not be detailed)
     * @param spawnpoints list of player spawnpoints, can be null
     * @return the GenResult passed in with its values modified
     */
    public GenResult generateTile(GenResult result, int sectorX, int sectorY, int localX, int localY, boolean detailed, Array<Point2> spawnpoints, Array<Item> ores){
        int x = sectorX * sectorSize + localX + Short.MAX_VALUE;
        int y = sectorY * sectorSize + localY + Short.MAX_VALUE;

        Block floor;
        Block wall = Blocks.air;

        double ridge = rid.getValue(x, y, 1f / 400f);
        double iceridge = rid.getValue(x+99999, y, 1f / 300f) + sim3.octaveNoise2D(2, 1f, 1f/14f, x, y)/11f;
        double elevation = elevationOf(x, y, detailed);
        double temp =
            + sim3.octaveNoise2D(detailed ? 12 : 9, 0.6, 1f / 1100f, x - 120, y);
        double lake = sim2.octaveNoise2D(1, 1, 1f / 110f, x, y);

        elevation -= Math.pow(lake + 0.15, 5);

        int lerpDst = 20;
        lerpDst *= lerpDst;
        float minDst = Float.MAX_VALUE;

        if(detailed && spawnpoints != null){
            for(Point2 p : spawnpoints){
                float dst = Mathf.dst2(p.x, p.y, localX, localY);
                minDst = Math.min(minDst, dst);

                if(dst < lerpDst){
                    float targetElevation = Math.max(0.86f, (float)elevationOf(sectorX * sectorSize + p.x + Short.MAX_VALUE, sectorY * sectorSize + p.y + Short.MAX_VALUE, true));
                    elevation = Mathf.lerp((float)elevation, targetElevation, Mathf.clamp(1.5f*(1f-(dst / lerpDst))));
                }
            }
        }

        if(elevation < 0.7){
            floor = Blocks.deepwater;
        }else if(elevation < 0.79){
            floor = Blocks.water;
        }else if(elevation < 0.85){
            floor = Blocks.sand;
        }else if (elevation < 2.5 && temp > 0.5){
            floor = Blocks.sand;
        }else if(temp < 0.42){
            floor = Blocks.snow;
        }else if(temp < 0.5){
            floor = Blocks.stone;
        }else if(temp < 0.6){
            floor = Blocks.grass;
        }else if(temp + ridge/2f < 0.8 || elevation < 1.3){
            floor = Blocks.blackStone;

            if(iceridge > 0.25 && minDst > lerpDst/1.5f){
                elevation ++;
            }
        }else if(minDst > lerpDst/1.5f){
            floor = Blocks.lava;
        }else{
            floor = Blocks.blackStone;
        }

        if(elevation > 3.3 && iceridge > 0.25 && temp < 0.6f){
            elevation ++;
            floor = Blocks.ice;
        }

        if(((Floor)floor).liquidDrop != null){
            elevation = 0;
        }

        if(detailed && wall == Blocks.air && decoration.containsKey(floor) && random.chance(0.03)){
            wall = decoration.get(floor);
        }

        if(ores != null && ((Floor) floor).hasOres){
            int offsetX = x - 4, offsetY = y + 23;
            for(int i = ores.size - 1; i >= 0; i--){
                Item entry = ores.get(i);
                if(Math.abs(0.5f - sim.octaveNoise2D(2, 0.7, 1f / (50 + i * 2), offsetX, offsetY)) > 0.23f &&
                Math.abs(0.5f - sim2.octaveNoise2D(1, 1, 1f / (40 + i * 4), offsetX, offsetY)) > 0.32f){
                    floor = OreBlock.get(floor, entry);
                    break;
                }
            }
        }

        result.wall = wall;
        result.floor = floor;
        result.elevation = (byte) Math.max(elevation, 0);
        return result;
    }

    double elevationOf(int x, int y, boolean detailed){
        double ridge = rid.getValue(x, y, 1f / 400f);
        return sim.octaveNoise2D(detailed ? 7 : 5, 0.62, 1f / 800, x, y) * 6.1 - 1 - ridge;
    }

    public static class GenResult{
        public Block floor, wall;
        public byte elevation;
    }

    public class OreEntry{
        final float frequency;
        final Item item;
        final Simplex noise;
        final RidgedPerlin ridge;
        final int index;

        OreEntry(Item item, float frequency, long seed){
            this.frequency = frequency;
            this.item = item;
            this.noise = new Simplex(seed + oreIndex);
            this.ridge = new RidgedPerlin((int)(seed + oreIndex), 2);
            this.index = oreIndex++;
        }
    }
}
