package mindustryV4.entities.traits;

public interface DrawTrait extends Entity{

    default float drawSize(){
        return 20f;
    }

    void draw();
}