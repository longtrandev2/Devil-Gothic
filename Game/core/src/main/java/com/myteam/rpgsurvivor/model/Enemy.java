package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;

public abstract class Enemy extends Entity {
    // Stat
    protected int level;
    //protected Rectangle hitBox;
    protected float attackCooldown;
    protected float attackTimer;
    protected Player targetPlayer;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;

    // Khoảng cách phát hiện và khoảng cách tấn công
    protected float detectionRange;
    protected float attackRange;

    private float offsetX;
    private float offsetY;

    public Enemy(float x, float y, MonsterType enemyType, Player player) {
        this.entityX = x;
        this.entityY = y;

        // Stat
        this.stat = enemyType.stat;
        this.level = 1;
        this.currentHealth = stat.maxHealth;

        // Khởi tạo hitbox
       this.hitbox = enemyType.hitbox.createHitbox(entityX,entityY);
       offsetX = enemyType.hitbox.getOffsetX();
       offsetY = enemyType.hitbox.getOffsetY();

        // Thiết lập player là mục tiêu
        this.targetPlayer = player;

        // Thiết lập các thuộc tính tấn công
        this.attackCooldown = 1f / stat.attackSpeed; // Thời gian giữa các đòn tấn công
        this.attackTimer = 0;
        this.attackRange = stat.rangeAttack;
        this.detectionRange = 1000f;

        this.movement = new EnemyMovement(x,y, player, stat.moveSpeed);

        this.isInvisible = false;
        this.isInvulnerable = false;
        this.isInteracting = false;
        this.isAttack = false;
    }

    @Override
    public void update(float deltaTime) {
        hitbox.setPosition(entityX + offsetX, entityY + offsetY);

        // Kiểm tra player có trong tầm phát hiện không
        if (isPlayerInDetectionRange()) {
            if (isPlayerInAttackRange()) {
                tryAttack();
            }
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }
    }

    // Thay đổi phương thức render để truyền deltaTime cho update
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        update(deltaTime);
    }

    public boolean isPlayerInDetectionRange() {
        float distance = Vector2.dst(entityX, entityY, targetPlayer.getHitbox().getX(), targetPlayer.getHitbox().getY());
        return distance <= detectionRange;
    }

    public boolean isPlayerInAttackRange() {
        return hitbox.overlaps(targetPlayer.hitbox);
        //System.out.println(hitBox + " " + targetPlayer.hitbox);

    }

    private void tryAttack() {

        if (attackTimer <= 0 && !isAttack) {
            performAttack();

            attackTimer = attackCooldown;
        }
    }

    protected void performAttack() {

    }

    public void setDetectionRange(float range) {
        this.detectionRange = range;
    }

    public void setAttackRange(float range) {
        this.attackRange = range;
    }

    public Rectangle getHitBox() {
        return hitbox;
    }
}
