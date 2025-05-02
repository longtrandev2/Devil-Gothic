package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

import java.awt.*;

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

    private ShapeRenderer shapeRenderer;


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

        this.hitbox = new Rectangle(x, y, 100, 50);

    }

    public void takeDamage(int damage)
    {
        onHurt();
    }

    public abstract void onHurt();







}
