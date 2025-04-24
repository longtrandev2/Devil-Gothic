package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.Attack;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.animation.AnimationManager;

public abstract class Entity {
    protected AnimationManager animationManager;
    protected int currentHealth;
    protected Movement movement;
    protected float entityX, entityY;
    protected Attack currentAttack;
    protected boolean isAttack;
    protected boolean isDead = false;
    protected boolean facingRight = true;

    //Stat
    protected EntityStat stat;
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

    //Set, get stat

    public int getMaxHealth() {
        return stat.maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.stat.maxHealth = maxHealth;
    }


    public int getDamage() {
        return stat.damage;
    }

    public void setDamage(int damage) {
        this.stat.damage = damage;
    }

    public float getMoveSpeed() {
        return stat.moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.stat.moveSpeed = moveSpeed;
    }

    public float getAttackSpeed(){
        return this.stat.attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed){
       this.stat.attackSpeed = attackSpeed;
    }

    public float getRangeAttack(){
        return this.stat.rangeAttack;
    }

    public void setRangeAttack(float rangeAttack){
        this.stat.rangeAttack = rangeAttack;
    }


    public float getWidth() {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        return currentFrame != null ? currentFrame.getRegionWidth() : 0;
    }

    public float getHeight() {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        return currentFrame != null ? currentFrame.getRegionHeight() : 0;
    }

    public void takeDamge(int damage){
        currentHealth -= damage;
        if(currentHealth <= 0){
            die();
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    private void die() {
        isDead = true;
    }
}
