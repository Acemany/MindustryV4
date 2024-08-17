package io.acemany.mindustryV4.type;

public class ItemStack{
    public io.acemany.mindustryV4.type.Item item;
    public int amount;

    public ItemStack(io.acemany.mindustryV4.type.Item item, int amount){
        this.item = item;
        this.amount = amount;
    }

    public boolean equals(ItemStack other){
        return other != null && other.item == item && other.amount == amount;
    }
}
