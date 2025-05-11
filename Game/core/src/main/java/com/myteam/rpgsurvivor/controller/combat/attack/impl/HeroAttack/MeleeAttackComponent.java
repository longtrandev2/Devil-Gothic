package com.myteam.rpgsurvivor.controller.combat.attack.impl.HeroAttack;

import com.myteam.rpgsurvivor.controller.combat.attack.impl.Attack;
import com.myteam.rpgsurvivor.model.Player;

public class BaseHeroAttack implements AttackComponent {
    private Player chosenHero;
    private int damage;
    private float range;
    private int speedAttack;
    private long lastAttackTime;

    public BaseHeroAttack(Player chosenHero) {
        this.chosenHero = chosenHero;
        this.damage = chosenHero.getDamage();
        this.range = chosenHero.getRangeAttack();
        this.speedAttack = 800;
        this.lastAttackTime = 0;
    }

    public void update(float deltaTime, List <Enemy> enemies) {

    }
    @Override
    public boolean executeAttack() {
        if (!canAttack()) {
            return false;
        }

        lastAttackTime = System.currentTimeMillis();

        return true;
    }

    @Override
    public int getCooldown() {
        return speedAttack;
    }

    @Override
    public boolean canAttack() {
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


    private boolean isEnemyInRange() {
        return true;
    }

    private void applyDamageToEnemy() {
        System.out.println("Enemy hit with damage: " + damage);
    }


}
