package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;

public class Archer extends Player {
    private InputHandle inputHandle;
    private Movement movement;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private boolean showSkill = false;
    private float skillX, skillY;

    private float skillStateTime = 0f;
    private AnimationManager skillEffectManager;

    private static final int IDLE_FRAME_COLS = 12;
    private static final int IDLE_FRAME_ROWS = 1;

    private static final int WALK_FRAME_COLS = 10;
    private static final int WALK_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;

    private static final int SKILL_FRAME_COLS = 12;
    private static final int SKILL_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 15;
    private static final int ATTACK_FRAME_ROWS = 1;

    private static final int SKILL_EFFECT_FRAME_COLS = 17;
    private static final int SKILL_EFFECT_FRAME_ROWS = 1;

    public static final String STATE_IDLE = "idle";
    public static final String STATE_WALK = "walk";
    public static final String STATE_ATTACK = "attack";
    public static final String STATE_SKILL = "skills";
    public static final String STATE_SKILL_EFFECT = "skillEffects";

    public Archer(float x, float y, int health, int damage, float speed)
    {
        super(x,y,health,damage,speed);
        this.animationManager = new AnimationManager();
        this.skillEffectManager = new AnimationManager();
        this.movement = new Movement(this);
        this.inputHandle = new InputHandle(this, movement);

        setupAnimation();
    }

    public void setupAnimation()
    {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = 0.08f;
        float skillFrameDuration = 0.08f;
        float skillEffectDuration = 0.1f;

        animationManager.addAnimation(
            STATE_IDLE,
            "Hero/Achers/spriteSheet/idle_1.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS,idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_WALK,
            "Hero/Achers/spriteSheet/run_1.png",
            WALK_FRAME_COLS, WALK_FRAME_ROWS,walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Hero/Achers/spriteSheet/atk.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS,attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_SKILL,
            "Hero/Achers/spriteSheet/atkSkill.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS,skillFrameDuration,
            false
        );

        skillEffectManager.addAnimation(
            STATE_SKILL_EFFECT,
            "Hero/Achers/spriteSheet/arrow_shower_effect_1.png",
            SKILL_EFFECT_FRAME_COLS, SKILL_EFFECT_FRAME_ROWS, skillEffectDuration,
            true
        );

    }
    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        stateTime += deltaTime;
        animationManager.update(deltaTime);

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
    public void update() {
        float deltaTime = 1/60f;
        updateWithDeltaTime(deltaTime);
    }

    public void updateWithDeltaTime(float deltaTime)
    {
        inputHandle.handleInput();

        if(inputHandle.isActionActive(InputHandle.ACTION_SKILL) && !isAttacking)
        {
            if(!isUsingSkill)
            {
                isUsingSkill = true;
                animationManager.setState(STATE_SKILL, true);

                showSkill = true;
                skillStateTime = 0f;
                skillX = entityX + (facingRight ? 100 : -100);
                skillY = entityY;

                skillEffectManager.setState(STATE_SKILL_EFFECT, true);
            }
        }
        movement.update();

        if (!isAttacking && !isUsingSkill) {
            if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK)) {
                isAttacking = true;
                animationManager.setState(STATE_ATTACK, true);
            }
        }

        updateAnimationState(deltaTime);


    }

    public void updateAnimationState(float deltaTime)
    {
        if (isUsingSkill) {
            if (animationManager.isAnimationFinished()) {
                isUsingSkill = false;
                if (movement.isMoving()) {
                    animationManager.setState(STATE_WALK, true);
                } else {
                    animationManager.setState(STATE_IDLE, true);
                }
            }
            return;
        }

        if (isAttacking) {
            if (animationManager.isAnimationFinished()) {
                isAttacking = false;
                if (movement.isMoving()) {
                    animationManager.setState(STATE_WALK, true);
                } else {
                    animationManager.setState(STATE_IDLE, true);
                }
            }
            return;
        }


        if (movement.isMoving()) {
            animationManager.setState(STATE_WALK, true);
        } else {
            animationManager.setState(STATE_IDLE, true);
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
}
