package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;
import com.myteam.rpgsurvivor.skills.WizardPortalSkill;

import java.util.ArrayList;


public class Wizard extends Player {

    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private boolean showSkill = false;
    private float skillX, skillY;


    private boolean enemyTurn = true;

    private WizardPortalSkill wizardPortalSkill;

    private static final int IDLE_FRAME_COLS = 6;
    private static final int IDLE_FRAME_ROWS = 1;

    private static final int RUN_FRAME_COLS = 8;
    private static final int RUN_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 8;
    private static final int ATTACK_FRAME_ROWS = 1;




    public Wizard(float x, float y) {
        super(x, y, HeroType.WIZARD);
        this.wizardPortalSkill = new WizardPortalSkill();
        setupAnimation();
    }

    public void setupAnimation() {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = this.getAttackSpeed();
        float skillFrameDuration = 0.08f;
        float skillEffectFrameDuration = 0.1f;

        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Hero/Wizard/Wizard Pack/SpriteSheet/Idle-Resize.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS, idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Hero/Wizard/Wizard Pack/SpriteSheet/Run-Resize.png",
            RUN_FRAME_COLS, RUN_FRAME_ROWS, walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Hero/Wizard/Wizard Pack/SpriteSheet/Attack1-Resize.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS, attackFrameDuration,
            false
        );



        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Hero/Wizard/Wizard Pack/SpriteSheet/Hit-Reisize.png",
            HURT_FRAME_COLS, HURT_FRAME_ROWS, skillEffectFrameDuration,
            true
        );

    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {

        stateTime += deltaTime;
        animationManager.update(deltaTime);

        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, entityX, entityY);
        }
                if(wizardPortalSkill.isActive())
                    wizardPortalSkill.render(batch, deltaTime);
    }

    @Override
    public void update(float deltaTime) {
        deltaTime = 1 / 60f;
        updateWithDeltaTime(deltaTime);
        wizardPortalSkill.update(deltaTime, enemyList);
        super.update(deltaTime);

    }

    public void updateWithDeltaTime(float deltaTime) {
        inputHandle.handleInput();

        if(isHurt)
        {
            hurtTimer -= deltaTime;
            if(hurtTimer <= 0)
            {
                isHurt = false;
            }
        }
        if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK) && attackHandler.canAttack()
            && !isAttacking && animationManager.getCurrentState().equals("idle")) {
            isAttacking = true;
            attackTriggered = false;
            animationManager.setState(StateType.STATE_ATTACK.stateType, true);
        }
        if (inputHandle.isActionActive(InputHandle.ACTION_SKILL) && !isAttacking && animationManager.getCurrentState().equals("idle")) {
            if (wizardPortalSkill.isReady()) {
                skillX = entityX + (facingRight ? 150 : -150);
                skillY = entityY;
                wizardPortalSkill.activate(skillX, skillY);
            }
        }
        if(!animationManager.getCurrentState().equals("attack"))
            heroMovement.update();


        updateAnimationState(deltaTime);
    }

    public void updateAnimationState(float deltaTime) {
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
            float progress = animationManager.getAnimationProgress();
            if (!attackTriggered && progress >= 0.85f) {
                attackHandler.tryAttack();
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
        wizardPortalSkill.upSkill();
    }
    @Override
    public  void decreaseSkill() {
        wizardPortalSkill.downSkill();

    }
}
