package com.myteam.rpgsurvivor.model.impl.Hero;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.myteam.rpgsurvivor.animation.AnimationForSummondKnight;
import com.myteam.rpgsurvivor.animation.AnimationManager;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.Entity;
import com.myteam.rpgsurvivor.model.SumondedPlayer;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;

public class SummonedKnight extends SumondedPlayer {
    private float lifeDuration = 20f;
    private float currentLifeTime = 0f;

    public SummonedKnight(float x, float y, HeroType heroType, Enemy enemy, AnimationForSummondKnight animation) {
        super(x, y, HeroType.SUMMON_KNIGHT, enemy, animation);
        this.currentLifeTime = 0f;
    }

    public boolean isLifeTimeOver() {
        return currentLifeTime >= lifeDuration;
    }

    public void updateTarget(Enemy newTarget) {
        if (targetEnemy.isDead() && newTarget != null) {
            this.targetEnemy = newTarget;
            this.hitboxEnemy = newTarget.getHitbox();
        }
    }

    @Override
    public void render(SpriteBatch batch, float deltaTime) {
        if (!isLifeTimeOver()) {
            super.render(batch, deltaTime);
        }
    }

    @Override
    public void update(float deltaTime) {
        currentLifeTime += deltaTime;

        if (!isLifeTimeOver()) {
            super.update(deltaTime);
        }
    }

    public float getRemainingLifeTime() {
        return Math.max(0, lifeDuration - currentLifeTime);
    }

    public Enemy getTargetEnemy() {
        return targetEnemy;
    }
}
