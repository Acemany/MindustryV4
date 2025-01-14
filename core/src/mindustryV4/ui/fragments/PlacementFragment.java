package mindustryV4.ui.fragments;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import mindustryV4.core.GameState.State;
import mindustryV4.entities.TileEntity;
import mindustryV4.game.EventType.WorldLoadGraphicsEvent;
import mindustryV4.graphics.Palette;
import mindustryV4.input.InputHandler;
import mindustryV4.type.Category;
import mindustryV4.type.ItemStack;
import mindustryV4.type.Recipe;
import mindustryV4.ui.ImageStack;
import mindustryV4.world.Block;
import mindustryV4.world.Tile;
import mindustryV4.world.blocks.OreBlock;
import ucore.core.Events;
import ucore.core.Graphics;
import ucore.scene.Group;
import ucore.scene.actions.Actions;
import ucore.scene.event.Touchable;
import ucore.scene.ui.ButtonGroup;
import ucore.scene.ui.Image;
import ucore.scene.ui.ImageButton;
import ucore.scene.ui.layout.Table;
import ucore.util.Bundles;

import static mindustryV4.Vars.*;

public class PlacementFragment extends Fragment{
    final int rowWidth = 4;

    Category currentCategory = Category.turret;
    Block hovered, lastDisplay;
    Tile hoverTile;
    Table blockTable, toggler, topTable;
    boolean shown = true;

    public PlacementFragment(){
        Events.on(WorldLoadGraphicsEvent.class, event -> {
            Group group = toggler.getParent();
            toggler.remove();
            build(group);
        });
    }

    @Override
    public void build(Group parent){
        parent.fill(full -> {
            toggler = full;
            full.bottom().right().visible(() -> !state.is(State.menu));

            full.table(frame -> {
                InputHandler input = control.input(0);

                //rebuilds the category table with the correct recipes
                Runnable rebuildCategory = () -> {
                    blockTable.clear();
                    blockTable.top().margin(5);

                    int index = 0;

                    ButtonGroup<ImageButton> group = new ButtonGroup<>();
                    group.setMinCheckCount(0);

                    for(Recipe recipe : Recipe.getByCategory(currentCategory)){

                        if(index++ % rowWidth == 0){
                            blockTable.row();
                        }

                        boolean[] unlocked = {false};

                        ImageButton button = blockTable.addImageButton("icon-locked", "select", 8*4, () -> {
                            if(control.unlocks.isUnlocked(recipe)){
                                input.recipe = input.recipe == recipe ? null : recipe;
                            }
                        }).size(46f).group(group).get();

                        button.update(() -> { //color unplacable things gray
                            boolean ulock = control.unlocks.isUnlocked(recipe);
                            TileEntity core = players[0].getClosestCore();
                            Color color = core != null && (core.items.has(recipe.requirements) || state.mode.infiniteResources) ? Color.WHITE : ulock ? Color.GRAY : Color.WHITE;
                            button.forEach(elem -> elem.setColor(color));
                            button.setChecked(input.recipe == recipe);

                            if(ulock == unlocked[0]) return;
                            unlocked[0] = ulock;

                            if(!ulock){
                                button.replaceImage(new Image("icon-locked"));
                            }else{
                                button.replaceImage(new ImageStack(recipe.result.getCompactIcon()));
                            }
                        });

                        button.hovered(() -> hovered = recipe.result);
                        button.exited(() -> {
                            if(hovered == recipe.result){
                                hovered = null;
                            }
                        });
                    }
                    blockTable.act(0f);
                };

                //top table with hover info
                frame.table("button-edge-2", top -> {
                    topTable = top;
                    top.add(new Table()).growX().update(topTable -> {
                        if((tileDisplayBlock() == null && lastDisplay == getSelected()) ||
                        (tileDisplayBlock() != null && lastDisplay == tileDisplayBlock())) return;

                        topTable.clear();
                        topTable.top().left().margin(5);

                        lastDisplay = getSelected();

                        if(lastDisplay != null){ //show selected recipe
                            topTable.table(header -> {
                                header.left();
                                header.add(new ImageStack(lastDisplay.getCompactIcon())).size(8*4);
                                header.labelWrap(() ->
                                    !control.unlocks.isUnlocked(Recipe.getByResult(lastDisplay)) ? Bundles.get("blocks.unknown") : lastDisplay.formalName)
                                    .left().width(190f).padLeft(5);
                                header.add().growX();
                                if(control.unlocks.isUnlocked(Recipe.getByResult(lastDisplay))){
                                    header.addButton("?", "clear-partial", () -> ui.content.show(Recipe.getByResult(lastDisplay)))
                                        .size(8 * 5).padTop(-5).padRight(-5).right().grow();
                                }
                            }).growX().left();
                            topTable.row();
                            //add requirement table
                            topTable.table(req -> {
                                req.top().left();

                                for(ItemStack stack : Recipe.getByResult(lastDisplay).requirements){
                                    req.table(line -> {
                                        line.left();
                                        line.addImage(stack.item.region).size(8*2);
                                        line.add(stack.item.localizedName()).color(Color.LIGHT_GRAY).padLeft(2).left();
                                        line.labelWrap(() -> {
                                            TileEntity core = players[0].getClosestCore();
                                            if(core == null || state.mode.infiniteResources) return "*/*";

                                            int amount = core.items.get(stack.item);
                                            String color = (amount < stack.amount / 2f ? "[red]" : amount < stack.amount ? "[accent]" : "[white]");

                                            return color + ui.formatAmount(amount) + "[white]/" + stack.amount;
                                        }).padLeft(5);
                                    }).left();
                                    req.row();
                                }
                            }).growX().left().margin(3);

                        }else if(tileDisplayBlock() != null){ //show selected tile
                            lastDisplay = tileDisplayBlock();
                            topTable.add(new ImageStack(lastDisplay.getDisplayIcon(hoverTile))).size(8*4);
                            topTable.labelWrap(lastDisplay.getDisplayName(hoverTile)).left().width(190f).padLeft(5);
                        }
                    });
                }).colspan(3).fillX().visible(() -> getSelected() != null || tileDisplayBlock() != null).touchable(Touchable.enabled);
                frame.row();
                frame.addImage("blank").color(Palette.accent).colspan(3).height(3*2).growX();
                frame.row();
                frame.table("pane-2", blocksSelect -> {
                    blocksSelect.margin(4).marginTop(0);
                    blocksSelect.table(blocks -> blockTable = blocks).grow();
                    blocksSelect.row();
                    blocksSelect.table(input::buildUI).growX();
                }).fillY().bottom().touchable(Touchable.enabled);
                frame.table(categories -> {
                    categories.defaults().size(50f);

                    ButtonGroup<ImageButton> group = new ButtonGroup<>();

                    for(Category cat : Category.values()){
                        if(Recipe.getByCategory(cat).isEmpty()) continue;

                        categories.addImageButton("icon-" + cat.name(), "clear-toggle",  16*2, () -> {
                            currentCategory = cat;
                            rebuildCategory.run();
                        }).group(group);

                        if(cat.ordinal() %2 == 1) categories.row();
                    }
                }).touchable(Touchable.enabled);

                rebuildCategory.run();
            });
        });
    }

    /**Returns the currently displayed block in the top box.*/
    Block getSelected(){
        Block toDisplay = null;

        Vector2 v = topTable.stageToLocalCoordinates(Graphics.mouse());

        //setup hovering tile
        if(!ui.hasMouse() && topTable.hit(v.x, v.y, false) == null){
            Tile tile = world.tileWorld(Graphics.mouseWorld().x, Graphics.mouseWorld().y);
            if(tile != null){
                hoverTile = tile.target();
            }else{
                hoverTile = null;
            }
        }else{
            hoverTile = null;
        }

        //block currently selected
        if(control.input(0).recipe != null){
            toDisplay = control.input(0).recipe.result;
        }

        //block hovered on in build menu
        if(hovered != null){
            toDisplay = hovered;
        }

        return toDisplay;
    }

    /**Returns the block currently being hovered over in the world.*/
    Block tileDisplayBlock(){
        return hoverTile == null ? null : hoverTile.block().synthetic() ? hoverTile.block() : hoverTile.floor() instanceof OreBlock ? hoverTile.floor() : null;
    }

    /**Show or hide the placement menu.*/
    void toggle(float t, Interpolation ip){
        toggler.clearActions();
        if(shown){
            shown = false;
            toggler.actions(Actions.translateBy(toggler.getTranslation().x + toggler.getWidth(), 0, t, ip));
        }else{
            shown = true;
            toggler.actions(Actions.translateBy(-toggler.getTranslation().x, 0, t, ip));
        }
    }
}