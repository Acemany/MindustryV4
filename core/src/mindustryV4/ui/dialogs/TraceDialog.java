package mindustryV4.ui.dialogs;

public class TraceDialog extends FloatingDialog{

    public TraceDialog(){
        super("$trace");

        addCloseButton();
    }
/*
    public void show(Player player, SessionInfo info){
        content().clear();

        Table table = new Table("clear");
        table.margin(14);
        table.defaults().pad(1);

        /*
        table.defaults().left();
        table.add(Bundles.format("trace.playername", player.name));
        table.row();
        table.add(Bundles.format("trace.ip", info.ip));
        table.row();
        table.add(Bundles.format("trace.id", info.uuid));
        table.row();
        table.add(Bundles.format("trace.modclient", info.modclient));
        table.row();
        table.add(Bundles.format("trace.android", info.android));
        table.row();

        table.add().pad(5);
        table.row();

        //disabled until further notice
/*
        table.add(Bundles.format("trace.totalblocksbroken", info.totalBlocksBroken));
        table.row();
        table.add(Bundles.format("trace.structureblocksbroken", info.structureBlocksBroken));
        table.row();
        table.add(Bundles.format("trace.lastblockbroken", info.lastBlockBroken.formalName));
        table.row();

        table.add().pad(5);
        table.row();

        table.add(Bundles.format("trace.totalblocksplaced", info.totalBlocksPlaced));
        table.row();
        table.add(Bundles.format("trace.lastblockplaced", info.lastBlockPlaced.formalName));
        table.row();

        content().add(table);

        show();
    }*/
}
