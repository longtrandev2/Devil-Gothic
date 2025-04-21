package com.myteam.rpgsurvivor.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Player extends Entity{

    protected int level;
    protected int experience;
    protected int mana;
    protected int skillPoints;
    protected int healthPoints;
    protected int damagePoints;
    protected int speedPoints;

    private final Vector2 positionVector = new Vector2();
    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;


    public Player(float x, float y, int health, int damage, float speed) {
        this.entityX = x;
        this.entityY = y;
        this.health = health;
        this.damage = damage;
        this.speed = speed;


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

    public Vector2 getPosition() {
        // Cập nhật và trả về biến thành viên positionVector
        // Đây là cách tối ưu hơn thay vì tạo 'new Vector2(entityX, entityY)' mỗi lần gọi
        return positionVector.set(this.entityX, this.entityY);
    }
    public void setPosition(float x, float y) {
        this.entityX = x;
        this.entityY = y;
    }
//    public abstract void update(float delta); // Đảm bảo có phương thức update
//    public abstract void render(SpriteBatch batch); // Đảm bảo có phương thức render
//    public abstract void dispose();

    public float getX() { return entityX; }
    public float getY() { return entityY; }


}
