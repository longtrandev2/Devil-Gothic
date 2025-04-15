package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.input.InputHandle;
import com.myteam.rpgsurvivor.controller.movement.Movement;
import com.myteam.rpgsurvivor.model.Player;

public class Samurai extends Player {

    private AnimationManager animationManager;
    private InputHandle inputHandler;
    private Movement movement;
    private boolean isAttack = false;
    private boolean facingRight = true;

    private static final int IDLE_FRAME_COLS = 10;
    private static final int IDLE_FRAME_ROWS = 1;
    private static final int WALK_FRAME_COLS = 16;
    private static final int WALK_FRAME_ROWS = 1;
    private static final int ATTACK_FRAME_COLS = 7;
    private static final int ATTACK_FRAME_ROWS = 1;

    public static final String STATE_IDLE = "idle";
    public static final String STATE_WALK = "walk";
    public static final String STATE_ATTACK = "attack";

    public Samurai(float x, float y, int health, int damage, float speed) {
        super(x, y, health, damage, speed);
        this.animationManager = new AnimationManager();
        this.movement = new Movement(this);
        this.inputHandler = new InputHandle(this, movement);

        setupAnimations();
    }

    private void setupAnimations() {
        float idleFrameDuration = 0.15f;
        float walkFrameDuration = 0.1f;
        float attackFrameDuration = 0.08f;

        animationManager.addAnimation(
            STATE_IDLE,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/IDLE.png",
            IDLE_FRAME_COLS, IDLE_FRAME_ROWS, idleFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_WALK,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/RUN.png",
            WALK_FRAME_COLS, WALK_FRAME_ROWS, walkFrameDuration,
            true
        );

        animationManager.addAnimation(
            STATE_ATTACK,
            "Hero/Samurai/FREE_Samurai 2D Pixel Art v1.2/Sprites/ATTACK 1.png",
            ATTACK_FRAME_COLS, ATTACK_FRAME_ROWS, attackFrameDuration,
            false
        );
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        animationManager.update(deltaTime);
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, entityX, entityY);
        }
    }

    @Override
    public void update() {
        inputHandler.handleInput();

        if (inputHandler.isActionActive(InputHandle.ACTION_ATTACK)) {
            isAttack = true;
        }

        if (isAttack && animationManager.isAnimationFinished()) {
            isAttack = false;
        }

        if (movement != null) {
            movement.update();
        }

        updateAnimationState();
    }

    private void updateAnimationState() {
        if (isAttack) {
            if (!animationManager.getCurrentState().equals(STATE_ATTACK)) {
                animationManager.setState(STATE_ATTACK, true);
            }
            return;
        }

        if (movement.isMoving()) {
            animationManager.setState(STATE_WALK, false);
        } else {
            animationManager.setState(STATE_IDLE, false);
        }

        animationManager.setFacingRight(facingRight);
    }

    @Override
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean getFacingRight() {
        return facingRight;
    }
}
