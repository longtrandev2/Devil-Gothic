package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.model.enum_type.StateType;


public abstract class Entity {
    protected AnimationManager animationManager;
    protected int currentHealth;
    protected Movement movement;
    protected float entityX, entityY;
    protected boolean isAttack;
    protected boolean isDead = false;
    protected boolean facingRight = true;
    protected Rectangle hitbox;
    protected Rectangle attackbox;
    //Stat
    protected EntityStat stat;

    protected float hurtTimer = 0;
    protected boolean isHurt = false;

    public abstract void render(SpriteBatch batch, float deltaTime);
    public abstract void update(float deltaTime);

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

    public void setMovement(Movement heroMovement) {
        this.movement = heroMovement;
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
        if(this.stat.attackSpeed <= 0.003) return 0.03f;
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

    public void die() {
        isDead = true;
    }

    public boolean isDead()
    {
        return isDead;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public void setState(String currentState){
        animationManager.setState(currentState, true);
    }

    public void setAttack(){
        isAttack = true;
    }
    public boolean isAttack(){
        return isAttack;
    }

    public Rectangle getAttackbox() {
        return attackbox;
    }

    public void setAttackbox(Rectangle attackbox) {
        this.attackbox = attackbox;
    }

    public void recover10PercentHealth() {
        this.setCurrentHealth(Math.min(this.getMaxHealth(), this.getCurrentHealth() + (int)(this.getMaxHealth() * 0.1f)));
        System.out.println("recover10PercentHealth" + this.currentHealth + "/" + this.getMaxHealth());

    }

    public void onHurt()
    {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);

    }
}
