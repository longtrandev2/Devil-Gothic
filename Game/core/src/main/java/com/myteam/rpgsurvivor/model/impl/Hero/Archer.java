package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;
import com.myteam.rpgsurvivor.model.impl.projectile.Arrow;
import com.myteam.rpgsurvivor.skills.ArcherBeamSkill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Archer extends Player {
//    private InputHandle inputHandle;
//    private HeroMovement heroMovement;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;


    private float skillX, skillY;


//    Arrow
    private List<Arrow> arrows = new ArrayList<>();
    private AnimationManager arrowAnimManager;
    private ArcherBeamSkill archerBeamSkill;

    private static final int IDLE_FRAME_COLS = 12;
    private static final int IDLE_FRAME_ROWS = 1;

    private static final int RUN_FRAME_COLS = 10;
    private static final int RUN_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;

    private static final int SKILL_FRAME_COLS = 12;
    private static final int SKILL_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 15;
    private static final int ATTACK_FRAME_ROWS = 1;

    private static final int HURT_COLS = 6;
    private static final int HURT_ROWS = 1;

    private static final int SKILL_EFFECT_FRAME_COLS = 17;
    private static final int SKILL_EFFECT_FRAME_ROWS = 1;


    private static final  int ARROW_FRAME_COLS = 1;
    private static final int ARROW_FRAME_ROWS = 1;
    public Archer(float x, float y)
    {
        super(x,y,HeroType.ARCHER);
        this.archerBeamSkill = new ArcherBeamSkill();
        setupAnimation();
    }

    public void setupAnimation()
    {
        float idleFrameDuration = 0.15f;
        float runFrameDuration = 0.1f;
        float attackFrameDuration = this.getAttackSpeed();
        float skillFrameDuration = 0.08f;
        float hurtFrameDuration = 0.1f;
        float skillEffectDuration = 0.1f;
        System.out.println(attackFrameDuration);
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Hero/Achers/spriteSheet/idle_1.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS,idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Hero/Achers/spriteSheet/run_1.png",
            RUN_FRAME_COLS, RUN_FRAME_ROWS,runFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Hero/Achers/spriteSheet/atk.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS,attackFrameDuration,
            false
        );


        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Hero/Achers/spriteSheet/take_hit_1.png",
            HURT_COLS, HURT_ROWS,hurtFrameDuration,
            false
        );


// Arrow animation
            arrowAnimManager = new AnimationManager();
            arrowAnimManager.addAnimation(
            "arrow",
            "Hero/Achers/aseprite/projectiles_and_effects.png",
            ARROW_FRAME_COLS, ARROW_FRAME_ROWS,
            0.1f,
            true
        );
    }
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        stateTime += deltaTime;
        animationManager.update(deltaTime);
        for (Arrow arrow : arrows) {
            arrow.render(batch);
        }

        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if(currentFrame != null)
        {
            batch.draw(
                currentFrame,
                entityX, entityY
            );
        }
        if(archerBeamSkill.isActive())
            archerBeamSkill.render(batch, deltaTime);
    }

    @Override
    public void update(float deltaTime) {
        deltaTime = 1/60f;
        updateWithDeltaTime(deltaTime);
        super.update(deltaTime);
        Iterator<Arrow> iter = arrows.iterator();
        while (iter.hasNext()) {
            Arrow arrow = iter.next();
            ArrayList<Enemy> enemyWave;
            if(enemySpawnController.isBossWave()) enemyWave = enemySpawnController.getActiveBoss();
                else enemyWave = enemySpawnController.getActiveEnemies();
            arrow.update(deltaTime, enemyWave, getDamage());
            if (arrow.isDestroyed()) {
                iter.remove();
            }
        }
        archerBeamSkill.update(deltaTime, enemyList);
    }

    public void updateWithDeltaTime(float deltaTime)
    {
        if(isHurt)
        {
            hurtTimer -= deltaTime;
            if(hurtTimer <= 0)
            {
                isHurt = false;
            }
        }

        inputHandle.handleInput();

        if(inputHandle.isActionActive(InputHandle.ACTION_SKILL)
            && !isAttacking && !isUsingSkill
            && animationManager.getCurrentState().equals("idle")) {

        if (archerBeamSkill.isReady()) {
            isUsingSkill = true;
            skillTriggered = false;
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }
    }

        if(!animationManager.getCurrentState().equals("attack"))
        heroMovement.update();

        if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK) && attackHandler.canAttack()
            && !isAttacking && animationManager.getCurrentState().equals("idle")) {
            isAttacking = true;
            attackTriggered = false;
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }

        updateAnimationState(deltaTime);

    }
    public void updateAnimationState(float deltaTime)
    {
        if (isUsingSkill) {
            float progress = animationManager.getAnimationProgress();

            if (!skillTriggered && progress >= 0.5f) {
                skillX = entityX + (facingRight ? +130 : 90);
                skillY = entityY + 20;
                archerBeamSkill.activate(skillX, skillY, isFacingRight());
                skillTriggered = true;
            }

            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                skillTriggered = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
            return;
        }

        if (isAttacking) {
            float progress = animationManager.getAnimationProgress();
            if (!attackTriggered && progress >= 0.5) {
                float arrowX = entityX + (facingRight ? + 95 : 65);
                float arrowY = entityY + 5;
                arrows.add(new Arrow(arrowX, arrowY, facingRight, arrowAnimManager));
                attackTriggered = true;
            }
            if (animationManager.isAnimationFinished()) {
                isAttacking = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
            return;
        }



        if (heroMovement.isMoving()) {
            animationManager.setState(StateType.STATE_RUN.stateType, true);
        } else {
            animationManager.setState(StateType.STATE_IDLE.stateType, true);
        }

        this.setFacingRight(facingRight);
        animationManager.setFacingRight(facingRight);
    }
    @Override
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    @Override
    public void onHurt() {
        isHurt = true;
        hurtTimer = 0.4f;
        animationManager.setState(StateType.STATE_HURT.stateType, true);

    }
    @Override
    public void updateSkill() {
      archerBeamSkill.upSkill();
    }

    @Override
    public  void decreaseSkill() {
        archerBeamSkill.downSkill();
    }
}
