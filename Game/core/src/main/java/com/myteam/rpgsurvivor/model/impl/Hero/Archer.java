package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.HeroMovement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;
import com.myteam.rpgsurvivor.model.impl.projectile.Arrow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Archer extends Player {
    private InputHandle inputHandle;
    private HeroMovement heroMovement;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private boolean showSkill = false;
    private float skillX, skillY;

    private float skillStateTime = 0f;
    private AnimationManager skillEffectManager;

//    Arrow
    private List<Arrow> arrows = new ArrayList<>();
    private AnimationManager arrowAnimManager;

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

    private static final int SKILL_EFFECT_FRAME_COLS = 17;
    private static final int SKILL_EFFECT_FRAME_ROWS = 1;


    private static final  int ARROW_FRAME_COLS = 1;
    private static final int ARROW_FRAME_ROWS = 1;
    public Archer(float x, float y)
    {
        super(x,y,HeroType.ARCHER);
        this.animationManager = new AnimationManager();
        this.skillEffectManager = new AnimationManager();
        this.heroMovement = new HeroMovement(this);
        this.inputHandle = new InputHandle(this, heroMovement);
        setupAnimation();
    }

    public void setupAnimation()
    {
        float idleFrameDuration = 0.15f;
        float runFrameDuration = 0.1f;
        float attackFrameDuration = this.getAttackSpeed();
        float skillFrameDuration = 0.08f;
        float skillEffectDuration = 0.1f;

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
            StateType.STATE_SKILL.stateType,
            "Hero/Achers/spriteSheet/atkSkill.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS,skillFrameDuration,
            false
        );

        skillEffectManager.addAnimation(
            StateType.STATE_SKILL_EFFECT.stateType,
            "Hero/Achers/spriteSheet/arrow_shower_effect_1.png",
            SKILL_EFFECT_FRAME_COLS, SKILL_EFFECT_FRAME_ROWS, skillEffectDuration,
            true
        );
// Arrow animation
            arrowAnimManager = new AnimationManager();
            arrowAnimManager.addAnimation(
            "arrow",
            "Hero/Achers/spriteSheet/arrow_.png",
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

        if(showSkill)
        {
            skillStateTime += deltaTime;
            skillEffectManager.update(deltaTime);

            TextureRegion skillEffectFrame = skillEffectManager.getCurrentFrame();
            if(skillEffectFrame != null)
            {
                batch.draw(skillEffectFrame, skillX, skillY);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        deltaTime = 1/60f;
        updateWithDeltaTime(deltaTime);
        super.update(deltaTime);
        Iterator<Arrow> iter = arrows.iterator();
        while (iter.hasNext()) {
            Arrow arrow = iter.next();
            arrow.update(deltaTime, enemySpawnController.getActiveEnemies(), getDamage()); // truyền enemyList và sát thương
            if (arrow.isDestroyed()) {
                iter.remove();
            }
        }

    }

    public void updateWithDeltaTime(float deltaTime)
    {
        inputHandle.handleInput();

        if(inputHandle.isActionActive(InputHandle.ACTION_SKILL) && !isAttacking)
        {
            if(!isUsingSkill)
            {
                isUsingSkill = true;
                animationManager.setState(StateType.STATE_SKILL.stateType, true);

                showSkill = true;
                skillStateTime = 0f;
                skillX = entityX + (facingRight ? 100 : -100);
                skillY = entityY;

                skillEffectManager.setState( StateType.STATE_SKILL_EFFECT.stateType, true);
            }
        }
        heroMovement.update();

        if (!isAttacking && !isUsingSkill && animationManager.getCurrentState().equals("idle")) {
            if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK)) {
                isAttacking = true;
                animationManager.setState(StateType.STATE_ATTACK.stateType, true);

                // Tạo mũi tên khi bắt đầu tấn công
                float arrowX = entityX + (facingRight ? +40 : -40);
                float arrowY = entityY + 20; // chỉnh theo vị trí tay
                arrows.add(new Arrow(arrowX, arrowY, facingRight, arrowAnimManager));
            }
        }

        updateAnimationState(deltaTime);


    }

    public void updateAnimationState(float deltaTime)
    {
        if (isUsingSkill) {
            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                if (heroMovement.isMoving()) {
                    animationManager.setState(StateType.STATE_RUN.stateType, true);
                } else {
                    animationManager.setState(StateType.STATE_IDLE.stateType, true);
                }
            }
            return;
        }

        if (isAttacking) {
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
}
