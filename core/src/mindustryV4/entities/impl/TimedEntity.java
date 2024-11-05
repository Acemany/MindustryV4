package mindustryV4.entities.impl;

import io.anuke.arc.util.pooling.Pool.Poolable;
import mindustryV4.entities.traits.*;

public abstract class TimedEntity extends BaseEntity implements ScaleTrait, TimeTrait, Poolable{
    public float time;

    @Override
    public void time(float time){
        this.time = time;
    }

    @Override
    public float time(){
        return time;
    }

    @Override
    public void update(){
        updateTime();
    }

    @Override
    public void reset(){
        time = 0f;
    }

    @Override
    public float fin(){
        return time() / lifetime();
    }
}