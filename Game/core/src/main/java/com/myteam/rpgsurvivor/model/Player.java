package com.myteam.rpgsurvivor.model;

import com.myteam.rpgsurvivor.model.enum_type.HeroType;

public abstract class Player extends Entity{

    protected int level;
    protected int experience;
    protected int mana;
    protected int skillPoints;
    protected int healthPoints;
    protected int damagePoints;
    protected int speedPoints;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;


    public Player(float x, float y, HeroType heroType) {
        this.entityX = x;
        this.entityY = y;

        this.stat = heroType.stat;

        this.level = 1;
        this.experience = 0;
        this.mana = 100;
        this.skillPoints = 0;
        this.healthPoints = 0;
        this.damagePoints = 0;
        this.speedPoints = 0;


        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;
    }


}
