package mindustryV4;

import io.anuke.arc.Application.ApplicationType;
import io.anuke.arc.Core;
import mindustryV4.entities.Entities;
import mindustryV4.entities.EntityGroup;
import mindustryV4.entities.impl.EffectEntity;
import mindustryV4.entities.traits.DrawTrait;
import io.anuke.arc.files.FileHandle;
import io.anuke.arc.graphics.Color;
import io.anuke.arc.util.Structs;
import mindustryV4.core.*;
import mindustryV4.entities.type.Player;
import mindustryV4.entities.type.TileEntity;
import mindustryV4.entities.bullet.Bullet;
import mindustryV4.entities.effect.Fire;
import mindustryV4.entities.effect.Puddle;
import mindustryV4.entities.traits.SyncTrait;
import mindustryV4.entities.type.BaseUnit;
import mindustryV4.game.GlobalData;
import mindustryV4.game.Team;
import mindustryV4.game.Version;
import mindustryV4.gen.Serialization;
import mindustryV4.net.Net;
import mindustryV4.world.blocks.defense.ForceProjector.ShieldEntity;

import java.util.*;

@SuppressWarnings("unchecked")
public class Vars{
    /**main application name, capitalized*/
    public static final String appName = "Mindustry V4";
    /**URL for telegram invite.*/
    public static final String telegramURL = "https://t.me/mindustryV4";
    /**URL for Github API for releases*/
    public static final String releasesURL = "https://api.github.com/repos/Acemany/MindustryV4Builds/releases";
    /**URL for Github API for contributors*/
    public static final String contributorsURL = "https://api.github.com/repos/Acemany/MindustryV4/contributors";
    /**URL for sending crash reports to*/
    public static final String crashReportURL = "http://mindustry.us.to/report";
    /**maximum distance between mine and core that supports automatic transferring*/
    public static final float mineTransferRange = 220f;
    /**team of the player by default*/
    public static final Team defaultTeam = Team.blue;
    /**team of the enemy in waves/sectors*/
    public static final Team waveTeam = Team.red;
    /**how many times longer a boss wave takes*/
    public static final float bossWaveMultiplier = 3f;
    /**how many times longer a launch wave takes*/
    public static final float launchWaveMultiplier = 2f;
    /**max chat message length*/
    public static final int maxTextLength = 150;
    /**max player name length in bytes*/
    public static final int maxNameLength = 40;
    /**displayed item size when ingame, TODO remove.*/
    public static final float itemSize = 5f;
    /**extra padding around the world; units outside this bound will begin to self-destruct.*/
    public static final float worldBounds = 100f;
    /**units outside of this bound will simply die instantly*/
    public static final float finalWorldBounds = worldBounds + 500;
    /**ticks spent out of bound until self destruct.*/
    public static final float boundsCountdown = 60*7;
    /**size of tiles in units*/
    public static final int tilesize = 8;
    /**size of sector*/
    public static final int sectorSize = 1024;
    /**all choosable player colors in join/host dialog*/
    public static final Color[] playerColors = {
        Color.valueOf("82759a"),
        Color.valueOf("c0c1c5"),
        Color.valueOf("fff0e7"),
        Color.valueOf("7d2953"),
        Color.valueOf("ff074e"),
        Color.valueOf("ff072a"),
        Color.valueOf("ff76a6"),
        Color.valueOf("a95238"),
        Color.valueOf("ffa108"),
        Color.valueOf("feeb2c"),
        Color.valueOf("ffcaa8"),
        Color.valueOf("008551"),
        Color.valueOf("00e339"),
        Color.valueOf("423c7b"),
        Color.valueOf("4b5ef1"),
        Color.valueOf("2cabfe"),
    };
    /**default server port*/
    public static final int port = 6567;
    /**if true, UI is not drawn*/
    public static boolean disableUI;
    /**visibility of fog*/
    public static boolean showFog = true;
    /**if true, game is set up in mobile mode, even on desktop. used for debugging*/
    public static boolean testMobile;
    /**whether the game is running on a mobile device*/
    public static boolean mobile;
    /**whether the game is running on an iOS device*/
    public static boolean ios;
    /**whether the game is running on an Android device*/
    public static boolean android;
    /**whether the game is running on a headless server*/
    public static boolean headless;
    /**application data directory, equivalent to {@link io.anuke.arc.Settings#getDataDirectory()}*/
    public static FileHandle dataDirectory;
    /**data subdirectory used for screenshots*/
    public static FileHandle screenshotDirectory;
    /**data subdirectory used for custom mmaps*/
    public static FileHandle customMapDirectory;
    /**data subdirectory used for saves*/
    public static FileHandle saveDirectory;
    /**map file extension*/
    public static final String mapExtension = "mmap";
    /**save file extension*/
    public static final String saveExtension = "msav";

    /**list of all locales that can be switched to*/
    public static Locale[] locales;

    public static ContentLoader content;
    public static GameState state;
    public static GlobalData data;

    public static Control control;
    public static Logic logic;
    public static Renderer renderer;
    public static UI ui;
    public static World world;
    public static NetServer netServer;
    public static NetClient netClient;

    public static EntityGroup<Player> playerGroup;
    public static EntityGroup<TileEntity> tileGroup;
    public static EntityGroup<Bullet> bulletGroup;
    public static EntityGroup<EffectEntity> effectGroup;
    public static EntityGroup<DrawTrait> groundEffectGroup;
    public static EntityGroup<ShieldEntity> shieldGroup;
    public static EntityGroup<Puddle> puddleGroup;
    public static EntityGroup<Fire> fireGroup;
    public static EntityGroup<BaseUnit>[] unitGroups;

    /**all local players, currently only has one player. may be used for local co-op in the future*/
    public static Player[] players = {};

    public static void init(){
        Serialization.init();

        //load locales
        String[] stra = Core.files.internal("locales").readString().split("\n");
        locales = new Locale[stra.length];
        for(int i = 0; i < locales.length; i++){
            String code = stra[i];
            if(code.contains("_")){
                locales[i] = new Locale(code.split("_")[0], code.split("_")[1]);
            }else{
                locales[i] = new Locale(code);
            }
        }

        Arrays.sort(locales, Structs.comparing(l -> l.getDisplayName(l), String.CASE_INSENSITIVE_ORDER));
        Version.init();

        content = new ContentLoader();

        playerGroup = Entities.addGroup(Player.class).enableMapping();
        tileGroup = Entities.addGroup(TileEntity.class, false);
        bulletGroup = Entities.addGroup(Bullet.class).enableMapping();
        effectGroup = Entities.addGroup(EffectEntity.class, false);
        groundEffectGroup = Entities.addGroup(DrawTrait.class, false);
        puddleGroup = Entities.addGroup(Puddle.class).enableMapping();
        shieldGroup = Entities.addGroup(ShieldEntity.class, false);
        fireGroup = Entities.addGroup(Fire.class).enableMapping();
        unitGroups = new EntityGroup[Team.all.length];

        for(Team team : Team.all){
            unitGroups[team.ordinal()] = Entities.addGroup(BaseUnit.class).enableMapping();
        }

        for(EntityGroup<?> group : Entities.getAllGroups()){
            group.setRemoveListener(entity -> {
                if(entity instanceof SyncTrait && Net.client()){
                    netClient.addRemovedEntity((entity).getID());
                }
            });
        }

        state = new GameState();
        data = new GlobalData();

        mobile = Core.app.getType() == ApplicationType.Android || Core.app.getType() == ApplicationType.iOS || testMobile;
        ios = Core.app.getType() == ApplicationType.iOS;
        android = Core.app.getType() == ApplicationType.Android;

        Core.settings.setAppName(appName);

        dataDirectory = Core.settings.getDataDirectory();
        screenshotDirectory = dataDirectory.child("screenshots/");
        customMapDirectory = dataDirectory.child("maps/");
        saveDirectory = dataDirectory.child("saves/");
    }
}
