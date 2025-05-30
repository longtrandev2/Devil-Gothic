package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

import java.util.List;

public class ArcherBeamSkill implements Skill {
    private float cooldownTime = 13f;
    private float cooldownRemaining = 0f;

    private boolean isActive = false;
    private float duration = 0.5f   ;
    private float elapsed = 0f;

    private float centerX, centerY;
    private boolean facingRight;

    private Rectangle hitBox;
    private boolean hasDealtDamage = false;

    private int damage = 50;

    private AnimationManager animationManager;

    public ArcherBeamSkill() {
        hitBox = new Rectangle();
        animationManager = new AnimationManager();
        animationManager.addAnimation(
            StateType.STATE_SKILL.stateType,
            "Hero/Achers/spriteSheet/beam_skill.png",
            5, 1, 0.1f, true
        );
        animationManager.setState(StateType.STATE_SKILL.stateType, true);
    }

    public void activate(float x, float y, boolean facingRight) {
        if (!isReady()) return;

        this.centerX = x;
        this.centerY = y;
        this.facingRight = facingRight;

        float beamWidth = 500f;
        float beamHeight = 20f;
        float offsetX = facingRight ? 0 : -beamWidth;

        hitBox.set(x + offsetX, y - beamHeight / 2f, beamWidth, beamHeight);

        isActive = true;
        elapsed = 0f;
        cooldownRemaining = cooldownTime;
        hasDealtDamage = false;
    }

    public void update(float deltaTime, List<Enemy> enemies) {
        if (cooldownRemaining > 0) {
            cooldownRemaining -= deltaTime;
        }

        if (!isActive) return;

        elapsed += deltaTime;
        if (elapsed >= duration) {
            isActive = false;
        }

        if (!hasDealtDamage) {
            for (Enemy e : enemies) {
                if (!e.isDead() && hitBox.overlaps(e.getHitBox())) {
                    e.takeDamge(damage);
                    e.onHurt();

                    float knockbackForce = 1500f;
                    float knockbackX = facingRight ? 1 : -1;
                    e.applyExternalForce(knockbackX * knockbackForce * deltaTime, 0);
                }
            }
            hasDealtDamage = true;
        }
    }

    public void render(SpriteBatch batch, float deltaTime) {
        if (!isActive) return;

        animationManager.update(deltaTime);
        float width = hitBox.width;
        float height = hitBox.height;

        batch.draw(
            animationManager.getCurrentFrame(),
            hitBox.x,
            hitBox.y,
            width,
            height
        );

        // Debug: show hitbox
        DebugRenderer.drawRect(hitBox, Color.GREEN);
    }

    @Override
    public float getCooldownRemaining() {
        return Math.max(0, cooldownRemaining);
    }

    @Override
    public float getCooldownPercent() {
        return cooldownRemaining / cooldownTime;
    }

    @Override
    public boolean isReady() {
        return cooldownRemaining <= 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void dispose() {

    }

    public void upSkill(){
        damage += 10;
        if(cooldownTime <= 8) return;
        cooldownTime -= 1;
    }

    public void downSkill(){
        damage -= 10;
        if(cooldownTime <= 8) return;
        cooldownTime += 1;
    }
}
