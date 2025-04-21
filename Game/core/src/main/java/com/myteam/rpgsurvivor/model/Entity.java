package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.Attack;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.animation.AnimationManager;

public abstract class Entity {
    protected AnimationManager animationManager;
    protected int health;
    protected int maxHealth;
    protected int damage;
    protected Movement movement;
    protected float speed;
    protected float entityX, entityY;
    protected Attack currentAttack;
    protected boolean isAttack;
    protected boolean isDead = false;
    protected boolean facingRight = true;



    public abstract void render(SpriteBatch batch, float deltaTime);
    public abstract void update();

    // Các getter và setter cần thiết cho Movement
    public float getEntityX() {
        return entityX;
    }

    public float getEntityY() {
        return entityY;
    }

    public void setEntityPosition(float x, float y) {
        this.entityX = x;
        this.entityY = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getWidth() {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        return currentFrame != null ? currentFrame.getRegionWidth() : 0;
    }

    public float getHeight() {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        return currentFrame != null ? currentFrame.getRegionHeight() : 0;
    }
}
