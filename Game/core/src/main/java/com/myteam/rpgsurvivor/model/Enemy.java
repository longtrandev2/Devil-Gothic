package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.math.Circle;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;

public abstract class Enemy extends Entity {
    //Stat
    protected int level;
    protected Circle hitBox;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;
    public Enemy(float x, float y, MonsterType enemyType) {
        this.entityX = x;
        this.entityY = y;

        //Stat
        this.stat = enemyType.stat;

        this.level = 1;

        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;
    }

}
