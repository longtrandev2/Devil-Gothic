package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForSummondKnight;
import com.myteam.rpgsurvivor.controller.EnemySpawnController;
import com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack.MeleeAttackComponent;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.controller.movement.SummonedKinghtMovement;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public abstract class SumondedPlayer extends Entity{
    protected int level;
    protected float attackCooldown;
    protected float attackTimer;
    protected Enemy targetEnemy;
    protected MeleeAttackComponent attackComponent;
    protected EnemySpawnController enemySpawnController;

    protected boolean isInvisible;
    protected boolean isInvulnerable;
    protected boolean isInteracting;


    protected float detectionRange;
    protected float attackRange;

    private float offsetX;
    private float offsetY;

    private StateType currentState;
    private SummonedKinghtMovement summonedKinghtMovement;
    private ShapeRenderer shapeRenderer;
    protected Rectangle hitboxPlayer;
    protected Rectangle hitboxEnemy;


    private Vector2 velocity = new Vector2();
    private float knockbackDecay = 10f;
    private float maxKnockbackSpeed = 300f;

    public SumondedPlayer (float x, float y, HeroType heroType, Enemy enemy, AnimationForSummondKnight animation)
    {
        this.entityX = x;
        this.entityY = y;

        this.stat = heroType.stat;
        this.currentHealth = stat.maxHealth;

        this.hitbox = heroType.hitbox.createHitbox(entityX, entityY);
        offsetX = heroType.hitbox.getOffsetX();
        offsetY = heroType.hitbox.getOffsetY();

        this.targetEnemy = enemy;

        hitboxPlayer = getHitbox();
        hitboxEnemy = targetEnemy.getHitbox();
        this.summonedKinghtMovement = new SummonedKinghtMovement(x,y,enemy,heroType.stat.moveSpeed);
        this.animationManager = animation.createMiniKnightAnimation(heroType);
        this.currentState = StateType.STATE_IDLE;

        shapeRenderer = new ShapeRenderer();

        this.attackCooldown = 1f / stat.attackSpeed;
        this.attackTimer = 0;
        this.attackRange = stat.rangeAttack;
        this.detectionRange = 1000f;

        this.movement = new SummonedKinghtMovement(x,y,enemy,stat.moveSpeed);

        this.attackbox = new Rectangle(hitbox);
        attackbox.setSize(hitbox.getWidth() + attackRange , hitbox.getHeight() + attackRange);
    }

    private void updateHealth(float health) {
        currentHealth = (int) Math.max(0, Math.min(health, getMaxHealth()));
    }

    @Override
    public void update(float deltaTime) {
        updateHealth(getCurrentHealth());
        if (!velocity.isZero()) {
            entityX += velocity.x * deltaTime;
            entityY += velocity.y * deltaTime;

            float decay = knockbackDecay * deltaTime;
            if (velocity.len() <= decay) {
                velocity.setZero();
            } else {
                velocity.scl(1 - decay / velocity.len());
            }
        }

        hitbox.setPosition(entityX + offsetX, entityY + offsetY);

        if(isFacingRight()){
            attackbox.setPosition(entityX + offsetX , entityY + offsetY);
        } else {
            attackbox.setPosition(entityX + offsetX - attackRange, entityY + offsetY);
        }

        if (isDead) return;

        if (isHurt) {
            hurtTimer -= deltaTime;
            if (hurtTimer <= 0) {
                isHurt = false;
            } else {
                currentState = StateType.STATE_HURT;
                animationManager.setState(StateType.STATE_HURT.stateType, true);
                return;
            }
        }

        // Kiểm tra player có trong tầm phát hiện không
        if (isEnemyInDetectionRange()) {
            if (isEnemyInAttackRange()) {
                tryAttack();
            }
        }

        if (attackTimer > 0) {
            attackTimer -= deltaTime;
        }

        float dx = 0;
        if(hitboxPlayer.getX() + hitboxPlayer.getWidth()/2 < hitboxEnemy.getX())
            dx = hitboxEnemy.getX() - (hitboxPlayer.getX() + hitboxPlayer.getWidth()/2);
        else if( hitboxEnemy.getX() + hitboxEnemy.getWidth() < hitboxPlayer.getX())
            dx = hitboxPlayer.getX() - (hitboxEnemy.getX() + hitboxEnemy.getWidth()/2);

        float dy = 0;
        if(hitboxPlayer.getY() + hitboxPlayer.getHeight()/2 < hitboxEnemy.getY())
            dx = hitboxEnemy.getY() - (hitboxPlayer.getX() + hitboxPlayer.getHeight()/2);
        else if( hitboxEnemy.getY() + hitboxEnemy.getHeight()/2 < hitboxPlayer.getY())
            dx = hitboxPlayer.getY() - (hitboxEnemy.getY() + hitboxEnemy.getHeight()/2);

        float shortestDistanceToPlayer = (float) Math.sqrt(dx * dx + dy * dy);

        if (shortestDistanceToPlayer <= detectionRange && shortestDistanceToPlayer > attackRange) {
            currentState = StateType.STATE_RUN;
            // Sử dụng deltaTime được truyền vào
            Vector2 newDirection = summonedKinghtMovement.move(deltaTime);
            entityX = newDirection.x;
            entityY = newDirection.y;
        }
        else if (attackbox.overlaps(hitboxEnemy)) {
            if (isAttack) {
                currentState = StateType.STATE_ATTACK;
            } else {
                currentState = StateType.STATE_IDLE;
            }
        } else {
            currentState = StateType.STATE_IDLE;
        }

        if (targetEnemy.getHitbox().x > entityX ) {
            //System.out.println(targetPlayer.getHitbox().x + " " + entityX);
            facingRight = true;
        }
        else {
            facingRight = false;
        }

        if (animationManager != null) {
            animationManager.setState(currentState.stateType, true);

            if (isAttack && animationManager.isAnimationFinished()) {
                isAttack = false;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        if (isDead || animationManager == null) return;

        update(deltaTime);

        animationManager.update(deltaTime);

        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);


        batch.draw(animationManager.getCurrentFrame(), entityX, entityY);


        DebugRenderer.drawRect(hitboxPlayer, Color.GREEN);
        DebugRenderer.drawRect(hitbox, Color.YELLOW);
        DebugRenderer.drawRect(attackbox, Color.RED);

    }

    public void onHurt() {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);
    }

    public boolean isEnemyInDetectionRange() {
        float distance = Vector2.dst(entityX, entityY, targetEnemy.getHitbox().getX(), targetEnemy.getHitbox().getY());
        return distance <= detectionRange;
    }

    public boolean isEnemyInAttackRange() {
        return attackbox.overlaps(hitboxEnemy);
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
            attackComponent.tryAttack();
        }

        if(currentState == StateType.STATE_ATTACK) {
//            targetEnemy.takeDamge(getDamage());
//            //System.out.println("takedamge");
//            targetEnemy.onHurt();
            attackComponent.tryAttack();
        }
    }

    public void setEnemySpawnController (EnemySpawnController controller)
    {

        this.enemySpawnController = controller;
        setMeleeAttackComponent();
    }

    public EnemySpawnController getEnemySpawnController() {
        return enemySpawnController;
    }

    public void setMeleeAttackComponent(){
        this.attackComponent = new MeleeAttackComponent(this, enemySpawnController, this.getAttackSpeed(), this.getRangeAttack(), this.getDamage());
        System.out.println(this.getDamage());
    }

    public void setMovementToEnemy(Enemy enemy)
    {
        summonedKinghtMovement = new SummonedKinghtMovement(entityX, entityY, enemy, stat.moveSpeed);
        movement = new SummonedKinghtMovement(entityX, entityY, enemy, stat.moveSpeed);
    }
}
