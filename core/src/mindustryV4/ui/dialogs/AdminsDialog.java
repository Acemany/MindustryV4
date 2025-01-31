package mindustryV4.ui.dialogs;

import mindustryV4.net.Administration.PlayerInfo;
import ucore.scene.ui.ScrollPane;
import ucore.scene.ui.layout.Table;

import static mindustryV4.Vars.*;

public class AdminsDialog extends FloatingDialog{

    public AdminsDialog(){
        super("$server.admins");

        addCloseButton();

        setup();
        shown(this::setup);
    }

    private void setup(){
        content().clear();

        float w = 400f, h = 80f;

        Table table = new Table();

        ScrollPane pane = new ScrollPane(table);
        pane.setFadeScrollBars(false);

        if(netServer.admins.getAdmins().size == 0){
            table.add("$server.admins.none");
        }

        for(PlayerInfo info : netServer.admins.getAdmins()){
            Table res = new Table("button");
            res.margin(14f);

            res.labelWrap("[LIGHT_GRAY]" + info.lastName).width(w - h - 24f);
            res.add().growX();
            res.addImageButton("icon-cancel", 14 * 3, () -> {
                ui.showConfirm("$confirm", "$confirmunadmin", () -> {
                    netServer.admins.unAdminPlayer(info.id);
                    playerGroup.forEach(player -> {
                        if(player.uuid.equals(info.id)){
                            player.isAdmin = false;
                        }
                    });
                    /*
                    for(Player player : playerGroup.all()){
                        if(player.con != null){
                            player.isAdmin = false;
                            break;
                        }
                    }*/
                    setup();
                });
            }).size(h).pad(-14f);

            table.add(res).width(w).height(h);
            table.row();
        }

        content().add(pane);
    }
}
