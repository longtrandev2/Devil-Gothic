package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.myteam.rpgsurvivor.animation.AnimationForSummondKnight;
import com.myteam.rpgsurvivor.model.Enemy;
import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.impl.Hero.SummonedKnight;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class KnightSkill {
    private static final int NUM_SUMMONS = 3;
    private static final float COOLDOWN_TIME = 2f;
    private AnimationForSummondKnight animation;

    private Knight owner;
    private List<SummonedKnight> summonedKnights;
    private float currentCooldown = 0f;
    private boolean isOnCooldown = false;
    Random rand = new Random();

    public KnightSkill(Knight owner) {
        this.owner = owner;
        this.summonedKnights = new ArrayList<>();
        this.animation = new AnimationForSummondKnight();
    }

    private ArrayList<Enemy> getTargetEnemies() {
        if (owner.getEnemySpawnController() != null) {
            return owner.getEnemySpawnController().getActiveEnemies();
        }
        return new ArrayList<>();
    }

    private ArrayList<Enemy> getTargetBosses() {
        if (owner.getEnemySpawnController() != null) {
            return owner.getEnemySpawnController().getActiveBoss();
        }
        return new ArrayList<>();
    }

    private Enemy getNearestEnemy(ArrayList<Enemy> enemies) {
        if (enemies.isEmpty()) return null;

        Enemy nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (enemy.isDead()) continue;

            float distance = (float) Math.sqrt(
                Math.pow(enemy.getEntityX() - owner.getEntityX(), 2) +
                    Math.pow(enemy.getEntityY() - owner.getEntityY(), 2)
            );

            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }
    private Enemy getRandomAliveEnemy(ArrayList<Enemy> enemies) {
        if (enemies.isEmpty()) return null;

        ArrayList<Enemy> aliveEnemies = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                aliveEnemies.add(enemy);
            }
        }

        if (aliveEnemies.isEmpty()) return null;

        return aliveEnemies.get(rand.nextInt(aliveEnemies.size()));
    }

    public boolean activateSkill() {
        if (!isOnCooldown && summonedKnights.size() < NUM_SUMMONS) {
            ArrayList<Enemy> enemies = getTargetEnemies();
            ArrayList<Enemy> bosses = getTargetBosses();

            if (enemies.isEmpty() && bosses.isEmpty()) {
                return false;
            }

            summonKnights();
            isOnCooldown = true;
            currentCooldown = COOLDOWN_TIME;
            return true;
        }
        return false;
    }

    private void summonKnights() {
        float[] angles = {0f, 120f, 240f};
        float distance = 1f;

        ArrayList<Enemy> targetBoss = getTargetBosses();
        ArrayList<Enemy> targetEnemy = getTargetEnemies();

        int knightsToSummon = NUM_SUMMONS;

        for (int i = 0; i < knightsToSummon; i++) {
            Enemy targetEnemyForKnight = null;

            if (!targetBoss.isEmpty()) {
                targetEnemyForKnight = getNearestEnemy(targetBoss);
            }

            if (targetEnemyForKnight == null && !targetEnemy.isEmpty()) {
                targetEnemyForKnight = getNearestEnemy(targetEnemy);
            }

            if (targetEnemyForKnight == null) {
                continue;
            }

            float angleRad = (float) Math.toRadians(angles[i]);
            float direction = owner.isFacingRight() ? 1 : -1;

            if (!owner.isFacingRight()) {
                angleRad = (float) Math.PI - angleRad;
            }

            float xOffset = distance * (float) Math.cos(angleRad) * direction;
            float yOffset = distance * (float) Math.sin(angleRad);

            float spawnX = owner.getEntityX() + xOffset;
            float spawnY = owner.getEntityY() + yOffset;

            SummonedKnight summonedKnight = new SummonedKnight(
                spawnX,
                spawnY,
                HeroType.SUMMON_KNIGHT,
                targetEnemyForKnight,
                animation
            );

            summonedKnight.setFacingRight(owner.isFacingRight());
            summonedKnights.add(summonedKnight);
        }
    }

    public void update(float deltaTime) {
        if (isOnCooldown) {
            currentCooldown -= deltaTime;
            if (currentCooldown <= 0) {
                isOnCooldown = false;
                currentCooldown = 0;
            }
        }

        Iterator<SummonedKnight> iterator = summonedKnights.iterator();
        while (iterator.hasNext()) {
            SummonedKnight knight = iterator.next();

            if (knight.getTargetEnemy().isDead()) {
                Enemy newTarget = findNewTarget();
                if (newTarget != null) {
                    knight.updateTarget(newTarget);
                }
            }

            knight.update(deltaTime);

            if (knight.isLifeTimeOver()) {
                iterator.remove();
                knight.die();
            }
        }
    }

    private Enemy findNewTarget() {
        ArrayList<Enemy> bosses = getTargetBosses();
        ArrayList<Enemy> enemies = getTargetEnemies();

        Enemy newTarget = getRandomAliveEnemy(bosses);
        if (newTarget == null) {
            newTarget = getRandomAliveEnemy(enemies);
        }

        return newTarget;
    }

    public void render(SpriteBatch batch, float deltaTime) {
        for (SummonedKnight knight : summonedKnights) {
            knight.render(batch, deltaTime);
        }
    }

    public boolean isActive() {
        return !summonedKnights.isEmpty();
    }

    public float getCooldownPercentage() {
        return isOnCooldown ? currentCooldown / COOLDOWN_TIME : 0;
    }

    public boolean isOnCooldown() {
        return isOnCooldown;
    }

    public int getActiveSummonCount() {
        return summonedKnights.size();
    }

    public void updatePositions() {

    }

    public void dismissAllSummons() {
        summonedKnights.clear();
    }
}
