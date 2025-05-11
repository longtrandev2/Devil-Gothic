package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class Player extends Entity{

    protected int level;
    protected int experience;
    protected int mana;
    protected int skillPoints;
    protected int healthPoints;
    protected int damagePoints;
    protected int speedPoints;
    protected EnemySpawnController enemySpawnController;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;

    private ShapeRenderer shapeRenderer;
    private float offsetX;
    private float offsetY;

    protected  MeleeAttackComponent attackHandler;

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
        this.currentHealth = stat.maxHealth;
        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;

        this.hitbox = heroType.hitbox.createHitbox(entityX , entityY);
        this.attackbox = new Rectangle(hitbox);
        offsetX = heroType.hitbox.getOffsetX();
        offsetY = heroType.hitbox.getOffsetY();
    }

    @Override
    public void update(float deltaTime){
        this.setCurrentHealth(max(0, this.currentHealth));
        hitbox.setPosition(entityX + offsetX  ,entityY + offsetY);
        DebugRenderer.drawRect(attackbox, Color.RED);
    }
    public abstract void onHurt();

    public void setEnemySpawnController (EnemySpawnController controller)
    {

        this.enemySpawnController = controller;
        setMeleeAttackComponent();
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemySpawnController.getActiveEnemies();
    }

    public void performAttack() {

    }
    public void setMeleeAttackComponent(){
        this.attackHandler = new MeleeAttackComponent(this, enemySpawnController, this.getAttackSpeed(), this.getRangeAttack(), this.getDamage());
    }
}
