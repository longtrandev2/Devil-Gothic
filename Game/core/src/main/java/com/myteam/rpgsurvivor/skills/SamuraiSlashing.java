package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.myteam.rpgsurvivor.debug.DebugRenderer;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.impl.Hero.Samurai;


public class SamuraiSlashing {
    private Samurai samurai;
    private boolean skillDamageTriggered = false;

    private Rectangle skillHitBox;
    private boolean showSkillHitBox = false;
    private float skillHitBoxTimer = 0f;
    private static final float SKILL_HITBOX_DURATION = 0.5f;
    private static final float SKILL_HITBOX_WIDTH = 100f;
    private static final float SKILL_HITBOX_HEIGHT = 80f;

    private int damgeSlash = 60;
    private ShapeRenderer shapeRenderer;

    public SamuraiSlashing(Samurai samurai)
    {
        this.samurai = samurai;
        this.shapeRenderer = new ShapeRenderer();
        this.skillHitBox = new Rectangle();
    }

    public boolean isSkillDamageTriggered() {
        return skillDamageTriggered;
    }

    public void setSkillDamageTriggered(boolean skillDamageTriggered) {
        this.skillDamageTriggered = skillDamageTriggered;
    }

    public void startSlash() {
        skillDamageTriggered = false;
        showSkillHitBox = true;
        skillHitBoxTimer = SKILL_HITBOX_DURATION;

        float x = samurai.getEntityX()
            + (samurai.isFacingRight()
            ? 40
            : -10);
        float y = samurai.getEntityY() + samurai.getHeight() * 0.5f - SKILL_HITBOX_HEIGHT * 0.5f;
        skillHitBox.set(x, y, SKILL_HITBOX_WIDTH, SKILL_HITBOX_HEIGHT);

    }

    public void update(float delta) {
        if (!showSkillHitBox) return;

        skillHitBoxTimer -= delta;
        if (skillHitBoxTimer <= 0f) {
            showSkillHitBox = false;
            return;
        }

        if (!skillDamageTriggered) {
            for (Enemy enemy : samurai.getEnemySpawnController().getActiveEnemies()) {
                if (skillHitBox.overlaps(enemy.getHitBox())) {
                    enemy.takeDamge(damgeSlash);
                    System.out.println(enemy.getCurrentHealth());
                    skillDamageTriggered = true;
                    break;
                }
            }
        }
    }

    public void render() {
        if (!showSkillHitBox) return;
        DebugRenderer.drawRect(skillHitBox,Color.RED);
    }

    public boolean isHitBoxVisible() {
        return showSkillHitBox;
    }
    public void upSkill(){
        damgeSlash += 20f;
    }


    public void downSkill(){
        damgeSlash -= 20f;
    }
}
