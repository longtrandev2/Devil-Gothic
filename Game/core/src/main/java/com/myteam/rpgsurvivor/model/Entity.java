package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.controller.combat.attack.Attack;
import com.myteam.rpgsurvivor.controller.movement.Movement;

public abstract class Entity {
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
}
