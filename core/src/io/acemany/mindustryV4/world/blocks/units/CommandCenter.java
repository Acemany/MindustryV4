package io.acemany.mindustryV4.world.blocks.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectSet;
import io.anuke.annotations.Annotations.Loc;
import io.anuke.annotations.Annotations.Remote;
import io.acemany.mindustryV4.content.fx.BlockFx;
import io.acemany.mindustryV4.entities.Player;
import io.acemany.mindustryV4.entities.TileEntity;
import io.acemany.mindustryV4.entities.units.BaseUnit;
import io.acemany.mindustryV4.entities.units.UnitCommand;
import io.acemany.mindustryV4.game.Team;
import io.acemany.mindustryV4.gen.Call;
import io.acemany.mindustryV4.graphics.Palette;
import io.acemany.mindustryV4.world.Block;
import io.acemany.mindustryV4.world.Tile;
import io.acemany.mindustryV4.world.meta.BlockFlag;
import io.anuke.ucore.core.Effects;
import io.anuke.ucore.core.Effects.Effect;
import io.anuke.ucore.graphics.Draw;
import io.anuke.ucore.scene.ui.ButtonGroup;
import io.anuke.ucore.scene.ui.ImageButton;
import io.anuke.ucore.scene.ui.layout.Table;
import io.anuke.ucore.util.EnumSet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static io.acemany.mindustryV4.Vars.*;

public class CommandCenter extends Block{
    protected TextureRegion[] commandRegions = new TextureRegion[UnitCommand.values().length];
    protected Color topColor = Palette.command;
    protected Color bottomColor = Color.valueOf("5e5e5e");
    protected Effect effect = BlockFx.commandSend;

    public CommandCenter(String name){
        super(name);

        flags = EnumSet.of(BlockFlag.comandCenter);
        destructible = true;
        solid = true;
        configurable = true;
    }

    @Override
    public void playerPlaced(Tile tile){
        ObjectSet<Tile> set = world.indexer.getAllied(tile.getTeam(), BlockFlag.comandCenter);

        if(set.size > 0){
            CommandCenterEntity entity = tile.entity();
            CommandCenterEntity oe = set.first().entity();
            entity.command = oe.command;
        }
    }

    @Override
    public void load(){
        super.load();

        for(UnitCommand cmd : UnitCommand.values()){
            commandRegions[cmd.ordinal()] = Draw.region("command-" + cmd.name());
        }
    }

    @Override
    public void draw(Tile tile){
        CommandCenterEntity entity = tile.entity();
        super.draw(tile);

        Draw.color(bottomColor);
        Draw.rect(commandRegions[entity.command.ordinal()], tile.drawx(), tile.drawy() - 1);
        Draw.color(topColor);
        Draw.rect(commandRegions[entity.command.ordinal()], tile.drawx(), tile.drawy());
        Draw.color();
    }

    @Override
    public void buildTable(Tile tile, Table table){
        CommandCenterEntity entity = tile.entity();
        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        Table buttons = new Table();

        for(UnitCommand cmd : UnitCommand.values()){
            buttons.addImageButton("command-" + cmd.name(), "clear-toggle", 8*3, () -> threads.run(() -> Call.onCommandCenterSet(players[0], tile, cmd)))
            .size(38f).checked(entity.command == cmd).group(group);
        }
        table.add(buttons);
        table.row();
        table.table("pane", t -> t.label(() -> entity.command.localized()).center().growX()).growX();
    }

    @Remote(called = Loc.server, forward = true, targets = Loc.both)
    public static void onCommandCenterSet(Player player, Tile tile, UnitCommand command){
        Effects.effect(((CommandCenter)tile.block()).effect, tile);

        for(Tile center : world.indexer.getAllied(tile.getTeam(), BlockFlag.comandCenter)){
            if(center.block() instanceof CommandCenter){
                CommandCenterEntity entity = center.entity();
                entity.command = command;
            }
        }

        Team team = (player == null ? tile.getTeam() : player.getTeam());

        for(BaseUnit unit : unitGroups[team.ordinal()].all()){
            unit.onCommand(command);
        }
    }

    @Override
    public TileEntity newEntity(){
        return new CommandCenterEntity();
    }

    public class CommandCenterEntity extends TileEntity{
        public UnitCommand command = UnitCommand.attack;

        @Override
        public void writeConfig(DataOutput stream) throws IOException{
            stream.writeByte(command.ordinal());
        }

        @Override
        public void readConfig(DataInput stream) throws IOException{
            command = UnitCommand.values()[stream.readByte()];
        }
    }
}
