package com.myteam.rpgsurvivor.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.myteam.rpgsurvivor.model.impl.Creep.SummonedKnight;
import com.myteam.rpgsurvivor.model.impl.Hero.Knight;

import java.util.ArrayList;
import java.util.List;

public class KnightSkill {
    private static final int NUM_SUMMONS = 3;
    private static final float SUMMON_DURATION = 10f;
    private static final float COOLDOWN_TIME = 15f;

    private Knight owner;
    private List<SummonedKnight> summonedKnights;
    private float currentCooldown = 0f;
    private boolean isOnCooldown = false;

    public KnightSkill(Knight owner) {
        this.owner = owner;
        this.summonedKnights = new ArrayList<>();
    }

    public boolean activateSkill() {
        if (!isOnCooldown && summonedKnights.isEmpty()) {
            summonKnights();
            isOnCooldown = true;
            currentCooldown = COOLDOWN_TIME;
            return true;
        }
        return false;
    }

    private void summonKnights() {
        // Calculate positions around the player
        float[] angles = {-30f, 0f, 30f}; // Place knights at different angles
        float distance = 50f; // Distance from player

        for (int i = 0; i < NUM_SUMMONS; i++) {
            // Calculate spawn position (in a semi-circle in front of player)
            float angleRad = (float) Math.toRadians(angles[i]);
            float direction = owner.isFacingRight() ? 1 : -1;

            // Adjust angle based on player direction
            if (!owner.isFacingRight()) {
                angleRad = (float) Math.PI - angleRad;
            }

            float xOffset = distance * (float) Math.cos(angleRad) * direction;
            float yOffset = distance * (float) Math.sin(angleRad);

            float spawnX = owner.getEntityX() + xOffset;
            float spawnY = owner.getEntityY() + yOffset;

            // Create the summoned knight with different stats based on index
            SummonedKnight summonedKnight = new SummonedKnight(
                spawnX,
                spawnY,
                10, // Half of owner's health
                owner.getDamage() / 2,  // Half of owner's damage
                owner.getSpeed() * 1.2f, // Slightly faster
                SUMMON_DURATION,
                i // Pass index to identify which animation to use
            );

            // Set the same direction as the player
            summonedKnight.setFacingRight(owner.isFacingRight());

            summonedKnights.add(summonedKnight);
        }
    }

    public void update(float deltaTime) {
        // Update cooldown
        if (isOnCooldown) {
            currentCooldown -= deltaTime;
            if (currentCooldown <= 0) {
                isOnCooldown = false;
                currentCooldown = 0;
            }
        }

        // Update summoned knights and remove dead ones
        for (int i = summonedKnights.size() - 1; i >= 0; i--) {
            SummonedKnight knight = summonedKnights.get(i);
            knight.update(deltaTime);


        }
    }
    public void render(SpriteBatch batch, float deltaTime) {
        // Render all active summoned knights
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

    // When the player moves, update the positions of the summoned knights to follow
    public void updatePositions() {
        if (!isActive()) return;

        float[] angles = {-30f, 0f, 30f};
        float distance = 50f;

        for (int i = 0; i < summonedKnights.size(); i++) {
            SummonedKnight knight = summonedKnights.get(i);

            // Calculate new position
            float angleRad = (float) Math.toRadians(angles[i]);
            float direction = owner.isFacingRight() ? 1 : -1;

            // Adjust angle based on player direction
            if (!owner.isFacingRight()) {
                angleRad = (float) Math.PI - angleRad;
            }

            float targetX = owner.getEntityX() + distance * (float) Math.cos(angleRad) * direction;
            float targetY = owner.getEntityY() + distance * (float) Math.sin(angleRad);

            // Apply movement toward target position (with slight delay for natural movement)
            Vector2 currentPos = new Vector2(knight.getEntityX(), knight.getEntityY());
            Vector2 targetPos = new Vector2(targetX, targetY);
            Vector2 direction2D = targetPos.sub(currentPos).nor().scl(knight.getSpeed() * 0.5f);

            if (currentPos.dst(targetPos) > 5f) {
                knight.setEntityPosition(knight.getEntityX() + direction2D.x, knight.getEntityY() + direction2D.y);

            }

            // Match the facing direction of the owner
            knight.setFacingRight(owner.isFacingRight());
        }
    }
}
