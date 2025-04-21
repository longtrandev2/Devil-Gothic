package com.myteam.rpgsurvivor.model.impl.Hero;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.model.Player;
import com.myteam.rpgsurvivor.skills.KnightSkill;

public class Knight extends Player{
    private KnightSkill knightSkill;
    private InputHandle inputHandle;
    private Movement movement;
    private boolean isAttacking = false;
    private boolean isUsingSkill = false;
    private float stateTime = 0;

    private static final int IDLE_FRAME_COLS = 7;
    private static final int IDLE_FRAME_ROWS = 1;

    private static final int WALK_FRAME_COLS = 8;
    private static final int WALK_FRAME_ROWS = 1;

    private static final int HURT_FRAME_COLS = 4;
    private static final int HURT_FRAME_ROWS = 1;

    private static final int SKILL_FRAME_COLS = 6;
    private static final int SKILL_FRAME_ROWS = 1;

    private static final int ATTACK_FRAME_COLS = 6;
    private static final int ATTACK_FRAME_ROWS = 1;

    public static final String STATE_IDLE = "idle";
    public static final String STATE_WALK = "walk";
    public static final String STATE_ATTACK = "attack";
    public static final String STATE_SKILL = "skills";

    public Knight(float x, float y, int health, int damage, float speed)
    {
        super(x,y,health,damage,speed);
        this.animationManager = new AnimationManager();
        this.movement = new Movement(this);
        this.inputHandle = new InputHandle(this, movement);
        this.knightSkill = new KnightSkill(this);

        setupAnimation();
    }

    private void setupAnimation()
    {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = 0.08f;
        float skillFrameDuration = 0.08f;


        animationManager.addAnimation(
            STATE_IDLE,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/IDLE.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS,idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_WALK,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/RUN.png",
            WALK_FRAME_COLS, WALK_FRAME_ROWS,walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/ATTACK 1.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS,attackFrameDuration,
            false
        );

        animationManager.addAnimation(
            STATE_SKILL,
            "Hero/Knight/Knight 2D Pixel Art/Sprites/with_outline/DEFEND.png",
            SKILL_FRAME_COLS, SKILL_FRAME_ROWS,skillFrameDuration,
            false
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

        knightSkill.render(batch, deltaTime);
    }

    @Override
    public void update() {
        float deltaTime = 1/60f;
        updateWithDeltaTime(deltaTime);
    }

    public void updateWithDeltaTime(float deltaTime)
    {
        knightSkill.update(deltaTime);
        if (!isAttacking) {
            inputHandle.handleInput();

            if (inputHandle.isActionActive(InputHandle.ACTION_ATTACK)) {
                isAttacking = true;
                animationManager.setState(STATE_ATTACK, true);
            }

            if (inputHandle.isActionActive(InputHandle.ACTION_SKILL)) {
                if(knightSkill.activateSkill())
                {
                    isUsingSkill = true;
                    animationManager.setState(STATE_SKILL, true);
                }
            }

            movement.update();

            if (movement.isMoving()) {
                knightSkill.updatePositions();
            }
        } else {
            inputHandle.handleInput();
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
