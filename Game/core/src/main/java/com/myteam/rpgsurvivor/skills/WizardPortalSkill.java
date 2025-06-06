package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

import java.util.List;
import java.util.PrimitiveIterator;

public class WizardPortalSkill implements Skill {
    private float cooldownTime = 15;
    private float cooldownRemaining = 0f;

    private boolean isActive = false;
    private float duration = 5f;
    private float elapsed = 0f;

    private float centerX, centerY;
    private float pullRadius = 35;
    private float pullStrength = 150f;

    private float damageAccumulator = 0f;
    private  float damagePerSecond = 10f;
    private float upSize = 0;
    private static final int   SKILL_EFFECT_FRAME_COLS = 7;
    private static final int SKILL_EFFECT_FRAME_ROWS = 1;
    private static final float skillFrameDuration = 0.1f;
    private AnimationManager animationManager;
//    private final Texture iconTexture;
    private  TextureRegion skillEffectTexture;
    private Circle hitboxPortal;
    public WizardPortalSkill() {
        animationManager =  new AnimationManager();
        animationManager.addAnimation(
            StateType.STATE_SKILL.stateType,
            "Skills/WizardSkill/Wizard Skill.png",
            SKILL_EFFECT_FRAME_COLS, SKILL_EFFECT_FRAME_ROWS, skillFrameDuration,
            true
        );
        this.animationManager.setState(StateType.STATE_SKILL.stateType, true);
//        this.iconTexture = new Texture("Skills/WizardSkill/SkillIcon.png");

    }

    public void activate(float x, float y) {
        if (!isReady()) return;
        this.centerX = x;
        this.centerY = y;
        hitboxPortal = new Circle(x, y, pullRadius);
        isActive = true;
        elapsed = 0f;
        cooldownRemaining = cooldownTime;
    }

    public void update(float deltaTime, List<Enemy> enemies) {
        if (cooldownRemaining > 0) {
            cooldownRemaining -= deltaTime;
        }

        if (isActive) {
            elapsed += deltaTime;
            if (elapsed >= duration) {
                isActive = false;
            }

            for (Enemy e : enemies) {
                float enemyX = e.getHitbox().getX();
                float enemyY =  e.getHitbox().getY();

                float dx = centerX - enemyX;
                float dy = centerY - enemyY;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);

                if (overlaps(hitboxPortal, e.getHitbox())) {
                    if (!e.isBoss()) {
                        float normX = dx / dist;
                        float normY = dy / dist;
                        float force = pullStrength * (1 - dist / pullRadius);
                        e.applyExternalForce(normX * force * deltaTime, normY * force * deltaTime);
                        // Gây damage tích lũy
                    }
                    damageAccumulator += damagePerSecond * deltaTime;
                    if (damageAccumulator >= 1f) {
                        int damageToApply = (int) damageAccumulator;
                        e.takeDamge(damageToApply);
                        e.onHurt();
                        damageAccumulator -= damageToApply;
                    }
                }

            }
        }
    }

    public void render(SpriteBatch batch, float deltaTime) {
        if (!isActive) return;
        animationManager.update(deltaTime);
        this.skillEffectTexture =
            animationManager.getCurrentFrame() ;

        float width = skillEffectTexture.getRegionWidth();
        float height = skillEffectTexture.getRegionHeight();
        batch.draw(skillEffectTexture, centerX - width / 2f, centerY - height / 2f, width + upSize, height + upSize);
        DebugRenderer.drawCircle(hitboxPortal, Color.WHITE);
    }

//    @Override
//    public Texture getIconTexture() {
//        return iconTexture;
//    }

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
//        iconTexture.dispose();
//        skillEffectTexture.dispose();
    }

   public void upSkill(){
        damagePerSecond += 5f;
        duration += 0.5f;
        pullRadius += 5f;
        upSize+=5f;
       if(cooldownTime <= 8) return;
       cooldownTime -= 0.5;
   }

    public void downSkill(){
        damagePerSecond -= 5f;
        duration -= 0.5f;
        pullRadius -= 5f;
        upSize -=5f;
        if(cooldownTime <= 8) return;
        cooldownTime += 0.5;
    }

    public static boolean overlaps(Circle circle, Rectangle rect) {
        float closestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float closestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));

        float dx = circle.x - closestX;
        float dy = circle.y - closestY;

        return (dx * dx + dy * dy) <= (circle.radius * circle.radius);
    }
}
