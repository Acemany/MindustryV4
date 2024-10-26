package mindustryV4.maps;

import com.badlogic.gdx.utils.Array;
import mindustryV4.content.Items;
import mindustryV4.content.Liquids;
import mindustryV4.content.Mechs;
import mindustryV4.content.UnitTypes;
import mindustryV4.content.blocks.CraftingBlocks;
import mindustryV4.content.blocks.ProductionBlocks;
import mindustryV4.content.blocks.UnitBlocks;
import mindustryV4.content.blocks.UpgradeBlocks;
import mindustryV4.entities.units.UnitCommand;
import mindustryV4.maps.missions.*;
import mindustryV4.type.Item;
import ucore.util.GridMap;
import ucore.util.Structs;

import static mindustryV4.Vars.mobile;

public class SectorPresets{
    private final GridMap<SectorPreset> presets = new GridMap<>();
    private final GridMap<Array<Item>> orePresets = new GridMap<>();

    public SectorPresets(){

        //base tutorial mission
        add(new SectorPreset(0, 0,
            TutorialSector.getMissions(),
            Array.with(Items.copper, Items.coal, Items.lead)));

        //command center mission
        add(new SectorPreset(0, 1,
            Structs.array(
                Missions.blockRecipe(UnitBlocks.daggerFactory),
                new UnitMission(UnitTypes.dagger),
                Missions.blockRecipe(UnitBlocks.commandCenter),
                new CommandMission(UnitCommand.retreat),
                new CommandMission(UnitCommand.attack),
                new BattleMission()
            ),
            Array.with(Items.copper, Items.lead, Items.coal)));

        //pad mission
        add(new SectorPreset(0, -2,
            Structs.array(
                Missions.blockRecipe(mobile ? UpgradeBlocks.alphaPad : UpgradeBlocks.dartPad),
                new MechMission(mobile ? Mechs.alpha : Mechs.dart),
                new WaveMission(15)
            ),
            Array.with(Items.copper, Items.lead, Items.coal, Items.titanium)));

        //oil mission
        add(new SectorPreset(-2, 0,
            Structs.array(
                Missions.blockRecipe(ProductionBlocks.cultivator),
                Missions.blockRecipe(ProductionBlocks.waterExtractor),
                new ContentMission(Items.biomatter),
                Missions.blockRecipe(CraftingBlocks.biomatterCompressor),
                new ContentMission(Liquids.oil),
                new BattleMission()
            ),
            Array.with(Items.copper, Items.lead, Items.coal, Items.titanium)));
    }

    public Array<Item> getOres(int x, int y){
        return orePresets.get(x, y);
    }

    public SectorPreset get(int x, int y){
        return presets.get(x, y);
    }

    public GridMap<SectorPreset> getPresets() { return presets; }

    private void add(SectorPreset preset){
        presets.put(preset.x, preset.y, preset);
        orePresets.put(preset.x, preset.y, preset.ores);
    }

    public static class SectorPreset{
        public final Array<Mission> missions;
        public final Array<Item> ores;
        public final int x, y;

        public SectorPreset(int x, int y, Array<Mission> missions, Array<Item> ores){
            this.missions = missions;
            this.x = x;
            this.y = y;
            this.ores = ores;
        }
    }
}
