package mindustryV4.ui.fragments;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import mindustryV4.core.GameState.State;
import mindustryV4.gen.Call;
import mindustryV4.graphics.Palette;
import mindustryV4.net.Net;
import mindustryV4.net.NetConnection;
import mindustryV4.net.Packets.AdminAction;
import ucore.core.Core;
import ucore.graphics.Draw;
import ucore.graphics.Lines;
import ucore.scene.Group;
import ucore.scene.event.Touchable;
import ucore.scene.ui.Image;
import ucore.scene.ui.layout.Table;
import ucore.scene.ui.layout.Unit;
import ucore.util.Bundles;
import ucore.util.Timer;

import static mindustryV4.Vars.*;

public class PlayerListFragment extends Fragment{
    private boolean visible = false;
    private Table content = new Table().marginRight(13f).marginLeft(13f);
    private Timer timer = new Timer();

    @Override
    public void build(Group parent){
        parent.fill(cont -> {
            cont.visible(() -> visible);
            cont.update(() -> {
                if(!(Net.active() && !state.is(State.menu))){
                    visible = false;
                    return;
                }

                if(visible && timer.get(20)){
                    rebuild();
                    content.pack();
                    content.act(Gdx.graphics.getDeltaTime());
                    //TODO hack
                    Core.scene.act(0f);
                }
            });

            cont.table("button", pane -> {
                pane.label(() -> Bundles.format(playerGroup.size() == 1 ? "players.single" : "players", playerGroup.size()));
                pane.row();
                pane.pane(content).grow().get().setScrollingDisabled(true, false);
                pane.row();

                pane.table(menu -> {
                    menu.defaults().growX().height(50f).fillY();

                    menu.addButton("$server.bans", ui.bans::show).disabled(b -> Net.client());
                    menu.addButton("$server.admins", ui.admins::show).disabled(b -> Net.client());
                    menu.addButton("$close", this::toggle);
                }).margin(0f).pad(10f).growX();

            }).touchable(Touchable.enabled).margin(14f);
        });

        rebuild();
    }

    public void rebuild(){
        content.clear();

        float h = 74f;

        playerGroup.all().sort((p1, p2) -> p1.getTeam().compareTo(p2.getTeam()));

        playerGroup.forEach(player -> {
            NetConnection connection = player.con;

            if(connection == null && Net.server() && !player.isLocal) return;

            Table button = new Table();
            button.left();
            button.margin(5).marginBottom(10);

            Table table = new Table(){
                @Override
                public void draw(Batch batch, float parentAlpha){
                    super.draw(batch, parentAlpha);
                    Draw.color(Palette.accent);
                    Draw.alpha(parentAlpha);
                    Lines.stroke(Unit.dp.scl(3f));
                    Lines.rect(x, y, width, height);
                    Draw.reset();
                }
            };
            table.margin(8);
            table.add(new Image(player.mech.iconRegion)).grow();

            button.add(table).size(h);
            button.labelWrap("[#" + player.color.toString().toUpperCase() + "]" + player.name).width(170f).pad(10);
            button.add().grow();

            button.addImage("icon-admin").size(14 * 2).visible(() -> player.isAdmin && !(!player.isLocal && Net.server())).padRight(5).get().updateVisibility();

            if((Net.server() || players[0].isAdmin) && !player.isLocal && (!player.isAdmin || Net.server())){
                button.add().growY();

                float bs = (h) / 2f;

                button.table(t -> {
                    t.defaults().size(bs);

                    t.addImageButton("icon-ban", "clear-partial", 14 * 2,
                        () -> ui.showConfirm("$confirm", "$confirmban", () -> Call.onAdminRequest(player, AdminAction.ban)));
                    t.addImageButton("icon-cancel", "clear-partial", 16 * 2,
                        () -> ui.showConfirm("$confirm", "$confirmkick", () -> Call.onAdminRequest(player, AdminAction.kick)));

                    t.row();

                    t.addImageButton("icon-admin", "clear-toggle", 14 * 2, () -> {
                        if(Net.client()) return;

                        String id = player.uuid;

                        if(netServer.admins.isAdmin(id, connection.address)){
                            ui.showConfirm("$confirm", "$confirmunadmin", () -> netServer.admins.unAdminPlayer(id));
                        }else{
                            ui.showConfirm("$confirm", "$confirmadmin", () -> netServer.admins.adminPlayer(id, player.usid));
                        }
                    })
                    .update(b -> b.setChecked(player.isAdmin))
                    .disabled(b -> Net.client())
                    .touchable(() -> Net.client() ? Touchable.disabled : Touchable.enabled)
                    .checked(player.isAdmin);

                    t.addImageButton("icon-zoom-small", "clear-partial", 14 * 2, () -> ui.showError("Currently unimplemented.")/*Call.onAdminRequest(player, AdminAction.trace)*/);

                }).padRight(12).size(bs + 10f, bs);
            }

            content.add(button).padBottom(-6).width(350f).maxHeight(h + 14);
            content.row();
            content.addImage("blank").height(3f).color(state.mode.isPvp ? player.getTeam().color : Palette.accent).growX();
            content.row();
        });

        content.marginBottom(5);
    }

    public void toggle(){
        visible = !visible;
        if(visible){
            rebuild();
        }
    }

}
