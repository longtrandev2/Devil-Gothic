package com.myteam.rpgsurvivor.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationManager {

    private HashMap<String, Animation<TextureRegion>> animations;

    private String currentState;

    private float stateTime;

    private boolean facingRight;

    public AnimationManager()
    {
        this.animations = new HashMap<>();
        this.currentState = "idle";
        this.stateTime = 0f;
        this.facingRight = true;
    }

    public void addAnimation(String stateName, String pathAnimation, int cols, int rows, float frameDuration, boolean loop)
    {
        Animation<TextureRegion> animation = AnimationLoader.getInstance().loadAnimation(pathAnimation, cols, rows, frameDuration);
        animation.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        animations.put(stateName, animation);

        if (animations.size() == 1) {
            currentState = stateName;
        }
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public void setState(String newState, boolean resetStateTime) {
        if (!animations.containsKey(newState)) {
            System.out.println("Warning: Animation state '" + newState + "' not found");
            return;
        }

        if (!currentState.equals(newState)) {
            currentState = newState;
            if (resetStateTime) {
                stateTime = 0;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
        if (currentAnimation == null) {
            if (!animations.isEmpty()) {
                currentAnimation = animations.values().iterator().next();
            } else {
                return null;
            }
        }

        TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);

        if (frame.isFlipX() != !facingRight) {
            frame.flip(true, false);
        }

        return frame;
    }


    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isAnimationFinished() {
        Animation<TextureRegion> currentAnimation = animations.get(currentState);
        return currentAnimation != null && currentAnimation.isAnimationFinished(stateTime);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void resetStateTime() {
        stateTime = 0;
    }

    public boolean hasState(String state) {
        return animations.containsKey(state);
    }
}
