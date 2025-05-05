package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public abstract class Enemy extends Entity {
    // Stat
    protected int level;
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

    private AnimationManager animationManager;
    private StateType currentState;
    private EnemyMovement enemyMovement;
    private ShapeRenderer shapeRenderer;
    private Rectangle hitboxPlayer;
    private Rectangle hitboxEnemy;

    public Enemy(float x, float y, MonsterType enemyType, Player player, AnimationForEnemy animationFactory) {
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

        entityX = x;
        entityY = y;
        hitboxPlayer = targetPlayer.getHitbox();
        hitboxEnemy = getHitBox();
        this.enemyMovement = new EnemyMovement(x,y,player,enemyType.stat.moveSpeed);
        this.animationManager = animationFactory.createEnemyAnimation(enemyType);
        this.currentState = StateType.STATE_IDLE;
        shapeRenderer = new ShapeRenderer();

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

        //AttackBox
        this.attackbox = new Rectangle(hitbox);
        attackbox.setSize(hitbox.getWidth() + attackRange * 2, hitbox.getHeight());
    }

    @Override
    public void update(float deltaTime) {
        hitbox.setPosition(entityX + offsetX, entityY + offsetY);
        attackbox.setPosition(entityX + offsetX - attackRange, entityY + offsetY);
        // Kiểm tra player có trong tầm phát hiện không
        if (isPlayerInDetectionRange()) {
            if (isPlayerInAttackRange()) {
                tryAttack();
            }
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        if (isDead) return;
        float dx = 0;
        //Tính hiệu dx nhỏ nhất
        if(hitboxEnemy.getX() + hitboxEnemy.getWidth()/2 < hitboxPlayer.getX())
            dx = hitboxPlayer.getX() - (hitboxEnemy.getX() + hitboxEnemy.getWidth()/2);
         else if(hitboxPlayer.getX() + hitboxPlayer.getWidth() < hitboxEnemy.getX())
            dx = hitboxEnemy.getX() - (hitboxPlayer.getX() + hitboxPlayer.getWidth()/2);

         float dy = 0;
        //Tính hiệu dy nhỏ nhất
        if(hitboxEnemy.getY() + hitboxEnemy.getHeight()/2 < hitboxPlayer.getY())
            dy = hitboxPlayer.getY() - (hitboxEnemy.getY() + hitboxEnemy.getHeight()/2);
        else if(hitboxPlayer.getY() + hitboxPlayer.getHeight()/2 < hitboxEnemy.getY())
            dy = hitboxEnemy.getY() - (hitboxPlayer.getY() + hitboxPlayer.getHeight()/2);

        float  shortestDistanceToPlayer = (float) Math.sqrt(dx * dx + dy * dy);

        if (shortestDistanceToPlayer <= detectionRange && shortestDistanceToPlayer > attackRange) {
            currentState = StateType.STATE_RUN;
            // Sử dụng deltaTime được truyền vào
            Vector2 newDirection = enemyMovement.move(deltaTime);
            entityX = newDirection.x;
            entityY = newDirection.y;
        }
        else if (attackbox.overlaps(hitboxPlayer)) {
            if (isAttack) {
                currentState = StateType.STATE_ATTACK;
            } else {
                currentState = StateType.STATE_IDLE;
            }
            //performAttacdsak();
        } else {
            currentState = StateType.STATE_IDLE;
        }

        if (targetPlayer.getHitbox().x > entityX) {
            facingRight = true;
        } else {
            facingRight = false;
        }

        if (animationManager != null) {
            animationManager.setState(currentState.stateType, true);

            if (isAttack && animationManager.isAnimationFinished()) {
                isAttack = false;
            }
        }
    }

    // Thay đổi phương thức render để truyền deltaTime cho update
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        if (isDead || animationManager == null) return;

        update(deltaTime);

        animationManager.update(deltaTime);


        // Thêm logic flip hình ảnh dựa trên hướng di chuyển
        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);

        batch.draw(animationManager.getCurrentFrame(), entityX, entityY);

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(hitboxPlayer.getX(), hitboxPlayer.getY(), hitboxPlayer.getWidth(), hitboxPlayer.getHeight());
        shapeRenderer.rect(hitboxEnemy.x, hitboxEnemy.y, hitboxEnemy.getWidth(), hitbox.getHeight());
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(attackbox.getX(), attackbox.getY(), attackbox.getWidth(), attackbox.getHeight());
        shapeRenderer.end();
        batch.begin();


        update(deltaTime);
    }

    public boolean isPlayerInDetectionRange() {
        float distance = Vector2.dst(entityX, entityY, targetPlayer.getHitbox().getX(), targetPlayer.getHitbox().getY());
        return distance <= detectionRange;
    }

    public boolean isPlayerInAttackRange() {
        return attackbox.overlaps(targetPlayer.hitbox);

    }

    private void tryAttack() {

        if (attackTimer <= 0 && !isAttack) {
            performAttack();

            attackTimer = attackCooldown;
        }
    }

    protected void performAttack() {
        isAttack = true;
        if (animationManager != null) {
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }

        if(currentState == StateType.STATE_ATTACK) {
            targetPlayer.takeDamge(getDamage());
            targetPlayer.onHurt();
        }
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
    public boolean isDead() {
        return isDead;
    }
}
