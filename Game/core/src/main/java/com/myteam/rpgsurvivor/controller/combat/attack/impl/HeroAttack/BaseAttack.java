package com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack;

import com.myteam.rpgsurvivor.controller.combat.attack.impl.Attack;
import com.myteam.rpgsurvivor.model.Player;

public class BaseAttack implements Attack {
    private Player chosenHero;
    private int damage;
    private float range;
    private int speedAttack;
    private long lastAttackTime;

    public BaseAttack(Player chosenHero) {
        this.chosenHero = chosenHero;
        this.damage = chosenHero.getDamage();
        this.range = chosenHero.getRangeAttack(); // Default attack range for samurai
        this.speedAttack = 800; // Default speedAttack (800ms)
        this.lastAttackTime = 0;
    }

    @Override
    public boolean executeAttack() {
        if (!canAttack()) {
            return false; // Can't attack if speedAttack is not finished
        }

        // Trigger samurai's attack animation only once attack is possible


        // Update last attack time to prevent immediate re-attacks
        lastAttackTime = System.currentTimeMillis();

        // Logic for detecting and damaging enemies within the attack range
        // Example logic for detecting enemy in range (this can be expanded based on your game logic)
        // For now, we just print that the attack is executed.
        System.out.println("Samurai attack executed! Damage: " + damage + ", Range: " + range);

        // For actual combat logic, you could check if an enemy is within range and apply damage.
        // Example (you would need access to the game world or enemy positions):
        // if (isEnemyInRange()) {
        //    applyDamageToEnemy();
        // }

        return true;
    }

    @Override
    public int getCooldown() {
        return speedAttack;
    }

    @Override
    public boolean canAttack() {
        // Return true if enough time has passed since the last attack
        return System.currentTimeMillis() - lastAttackTime >= speedAttack;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public float getRange() {
        return range;
    }

    // Placeholder method to simulate enemy detection within range (expand this as needed)
    private boolean isEnemyInRange() {
        // Logic for checking if the enemy is within the attack range
        // You would typically need access to enemy positions and check their distance from the samurai.
        return true; // For now, we just return true as a placeholder.
    }

    // Placeholder method to apply damage to an enemy (you would need to implement this with enemy logic)
    private void applyDamageToEnemy() {
        // Logic for dealing damage to enemies (e.g., reduce enemy health, trigger effects, etc.)
        System.out.println("Enemy hit with damage: " + damage);
    }


}
