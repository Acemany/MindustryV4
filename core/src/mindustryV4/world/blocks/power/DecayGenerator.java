package mindustryV4.world.blocks.power;

import mindustryV4.type.Item;

public class DecayGenerator extends ItemGenerator{

    public DecayGenerator(String name){
        super(name);
        hasItems = true;
        hasLiquids = false;
    }

    @Override
    protected float getItemEfficiency(Item item){
        return item.radioactivity;
    }
}
