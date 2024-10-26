package mindustryV4.world.blocks;

import com.badlogic.gdx.utils.Array;
import mindustryV4.type.Item;
import ucore.function.Consumer;
import ucore.function.Supplier;
import ucore.graphics.Draw;
import ucore.scene.style.TextureRegionDrawable;
import ucore.scene.ui.ButtonGroup;
import ucore.scene.ui.ImageButton;
import ucore.scene.ui.layout.Table;

import static mindustryV4.Vars.*;

public interface SelectionTrait{

    default void buildItemTable(Table table, Supplier<Item> holder, Consumer<Item> consumer){
        buildItemTable(table, false, holder, consumer);
    }

    default void buildItemTable(Table table, boolean nullItem, Supplier<Item> holder, Consumer<Item> consumer){

        Array<Item> items = content.items();

        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        Table cont = new Table();
        cont.defaults().size(38);

        int i = 0;

        if(nullItem){
            ImageButton button = cont.addImageButton("white", "clear-toggle", 24, () -> consumer.accept(null)).group(group).get();
            button.getStyle().imageUp = new TextureRegionDrawable(Draw.region("icon-nullitem"));
            button.setChecked(holder.get() == null);

            i ++;
        }

        for(Item item : items){
            if(!control.unlocks.isUnlocked(item)) continue;

            ImageButton button = cont.addImageButton("white", "clear-toggle", 24, () -> consumer.accept(item))
                    .group(group).get();
            button.getStyle().imageUp = new TextureRegionDrawable(item.region);
            button.setChecked(holder.get() == item);

            if(i++ % 4 == 3){
                cont.row();
            }
        }

        table.add(cont);
    }
}
