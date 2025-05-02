package com.myteam.rpgsurvivor.model.impl.Creep;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class Goblin extends Enemy {
    private AnimationManager animationManager;
    private StateType currentState;
    private EnemyMovement enemyMovement;
    private ShapeRenderer shapeRenderer;

    public Goblin(float x, float y, Player player, AnimationForEnemy animationFactory) {
        super(x, y, MonsterType.GOBLIN, player);
        entityX = x;
        entityY = y;
        this.enemyMovement = new EnemyMovement(x,y,player,MonsterType.GOBLIN.stat.moveSpeed);
        this.animationManager = animationFactory.createEnemyAnimation(MonsterType.GOBLIN);
        this.currentState = StateType.STATE_IDLE;
        shapeRenderer = new ShapeRenderer();



    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDead) return;

        float distanceToPlayer = Vector2.dst(entityX, entityY, targetPlayer.getEntityX(), targetPlayer.getEntityY());
        System.out.println(distanceToPlayer);

        if (distanceToPlayer <= detectionRange && distanceToPlayer > attackRange) {
            currentState = StateType.STATE_RUN;

            // Sử dụng deltaTime được truyền vào
            Vector2 newDirection = enemyMovement.move(deltaTime);
            entityX = newDirection.x;
            entityY = newDirection.y;

            // Cập nhật hướng nhìn dựa vào hướng di chuyển
            if (targetPlayer.getEntityX() > entityX) {
                facingRight = true;
            } else {
                facingRight = false;
            }
        } else if (distanceToPlayer <= attackRange) {
            if (isAttack) {
                currentState = StateType.STATE_ATTACK;
            } else {
                currentState = StateType.STATE_IDLE;
            }
        } else {
            currentState = StateType.STATE_IDLE;
        }

        if (animationManager != null) {
            animationManager.setState(currentState.stateType, true);

            if (isAttack && animationManager.isAnimationFinished()) {
                isAttack = false;
            }
        }
    }

    @Override
    protected void performAttack() {
        isAttack = true;
        if (animationManager != null) {
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }

        targetPlayer.takeDamge(getDamage());
        targetPlayer.onHurt();
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        if (isDead || animationManager == null) return;


        update(deltaTime);

        animationManager.update(deltaTime);

        float drawX = entityX - animationManager.getCurrentFrame().getRegionWidth() / 2;
        float drawY = entityY - animationManager.getCurrentFrame().getRegionHeight() / 4;

        // Thêm logic flip hình ảnh dựa trên hướng di chuyển
        boolean flipX = !facingRight;
        float width = animationManager.getCurrentFrame().getRegionWidth();
        float height = animationManager.getCurrentFrame().getRegionHeight();

        if (flipX) {
            batch.draw(
                animationManager.getCurrentFrame(),
                drawX + width,
                drawY,
                -width,
                height
            );
        } else {
            batch.draw(
                animationManager.getCurrentFrame(),
                drawX,
                drawY,
                width,
                height
            );
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(targetPlayer.getEntityX(), targetPlayer.getEntityY(),
            targetPlayer.getWidth(), targetPlayer.getHeight());
        shapeRenderer.circle(entityX, entityY,
            10f);
        shapeRenderer.end();


    }



    @Override
    public boolean isDead() {
        return isDead;
    }
}
