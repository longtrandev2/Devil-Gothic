package com.myteam.rpgsurvivor.model.impl.Creep;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class Rat extends Enemy {
    private AnimationManager animationManager;
    private StateType currentState;
    private EnemyMovement enemyMovement;
    private Rectangle hitboxPlayer;
    private Rectangle hitboxEnemy;
    private ShapeRenderer shapeRenderer;

    public Rat(float x, float y, Player player, AnimationForEnemy animationFactory)
    {
        super(x,y, MonsterType.RAT, player);
        this.enemyMovement = new EnemyMovement(x,y,player,MonsterType.RAT.stat.moveSpeed);
        this.animationManager = animationFactory.createEnemyAnimation(MonsterType.RAT);
        this.currentState = StateType.STATE_IDLE;
        hitboxPlayer = targetPlayer.getHitbox();
        hitboxEnemy = getHitbox();
        shapeRenderer = new ShapeRenderer();


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDead) return;

        float distanceToPlayer = Vector2.dst(entityX, entityY, hitboxPlayer.getX(), hitboxPlayer.getY());
        if (distanceToPlayer <= detectionRange && distanceToPlayer > attackRange) {
            currentState = StateType.STATE_RUN;

            // Sử dụng deltaTime được truyền vào
            Vector2 newDirection = enemyMovement.move(deltaTime);
            entityX = newDirection.x;
            entityY = newDirection.y;

            // Cập nhật hướng nhìn dựa vào hướng di chuyển

        }
        else if (hitboxEnemy.overlaps(hitboxPlayer)) {
            System.out.println(distanceToPlayer + " " + attackRange);
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

//        float drawX = entityX - animationManager.getCurrentFrame().getRegionWidth() / 2;
//        float drawY = entityY - animationManager.getCurrentFrame().getRegionHeight() / 4;

        // Thêm logic flip hình ảnh dựa trên hướng di chuyển
        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);

        batch.draw(animationManager.getCurrentFrame(), entityX, entityY);

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,1,0,1);
        shapeRenderer.rect(hitboxPlayer.getX(), hitboxPlayer.getY(), hitboxPlayer.getWidth(), hitboxPlayer.getHeight());
        shapeRenderer.rect(hitboxEnemy.x, hitboxEnemy.y, hitboxEnemy.getWidth(), hitbox.getHeight());
        shapeRenderer.end();
        batch.begin();


    }



    @Override
    public boolean isDead() {
        return isDead;
    }
}
