package mindustryV4.maps.generators;

import mindustryV4.game.Team;
import mindustryV4.maps.Sector;
import mindustryV4.type.Item;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.production.Drill;
import io.anuke.arc.util.Structs;
import io.anuke.arc.math.RandomXS128;

public class Generation{
    public final Sector sector;
    public final Tile[][] tiles;
    public final int width, height;
    public final RandomXS128 random;

    public Generation(Sector sector, Tile[][] tiles, int width, int height, RandomXS128 random){
        this.sector = sector;
        this.tiles = tiles;
        this.width = width;
        this.height = height;
        this.random = random;
    }

    Tile tile(int x, int y){
        if(!Structs.inBounds(x, y, tiles)){
            return null;
        }
        return tiles[x][y];
    }

    Item drillItem(int x, int y, Drill block){
        if(block.isMultiblock()){
            Item result = null;
            int offsetx = -(block.size - 1) / 2;
            int offsety = -(block.size - 1) / 2;

            for(int dx = 0; dx < block.size; dx++){
                for(int dy = 0; dy < block.size; dy++){
                    int worldx = dx + offsetx + x;
                    int worldy = dy + offsety + y;
                    if(!Structs.inBounds(worldx, worldy, tiles)){
                        return null;
                    }

                    if(!block.isValid(tiles[worldx][worldy]) || tiles[worldx][worldy].floor().itemDrop == null) continue;

                    Item drop = tiles[worldx][worldy].floor().itemDrop;

                    if(result == null || drop.id < result.id){
                        result = drop;
                    }
                }
            }
            return result;
        }else{
            return tiles[x][y].floor().itemDrop;
        }
    }



    public boolean canPlace(int x, int y, Block block){
        if(block.isMultiblock()){
            int offsetx = -(block.size - 1) / 2;
            int offsety = -(block.size - 1) / 2;

            for(int dx = 0; dx < block.size; dx++){
                for(int dy = 0; dy < block.size; dy++){
                    int worldx = dx + offsetx + x;
                    int worldy = dy + offsety + y;
                    if(!Structs.inBounds(worldx, worldy, tiles) || !tiles[worldx][worldy].block().alwaysReplace || tiles[worldx][worldy].floor().isLiquid){
                        return false;
                    }
                }
            }
            return true;
        }else{
            return tiles[x][y].block().alwaysReplace && !tiles[x][y].floor().isLiquid;
        }
    }

    public void setBlock(int x, int y, Block block, Team team){
        tiles[x][y].setBlock(block, team);
        if(block.isMultiblock()){
            int offsetx = -(block.size - 1) / 2;
            int offsety = -(block.size - 1) / 2;

            for(int dx = 0; dx < block.size; dx++){
                for(int dy = 0; dy < block.size; dy++){
                    int worldx = dx + offsetx + x;
                    int worldy = dy + offsety + y;
                    if(!(worldx == x && worldy == y) && Structs.inBounds(worldx, worldy, tiles)){
                        Tile toplace = tiles[worldx][worldy];
                        if(toplace != null){
                            toplace.setLinked((byte) (dx + offsetx), (byte) (dy + offsety));
                            toplace.setTeam(team);
                        }
                    }
                }
            }
        }
    }
}
