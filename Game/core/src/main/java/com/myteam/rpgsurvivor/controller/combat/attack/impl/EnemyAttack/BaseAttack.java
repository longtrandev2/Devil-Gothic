package com.myteam.rpgsurvivor.controller.combat.attack.impl.EnemyAttack;

import com.myteam.rpgsurvivor.controller.combat.attack.impl.Attack;
import com.myteam.rpgsurvivor.model.Entity;
import com.myteam.rpgsurvivor.model.Player;

public class BaseAttack implements Attack {
    private final Entity attacker;
    private final Player target;
    private final int damage;

    public BaseAttack(Entity attacker, Player target, int damage)
    {
        this.attacker = attacker;
        this.target = target;
        this.damage = damage;
    }
    @Override
    public boolean executeAttack() {
        target.takeDamge(damage);
        return true;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public boolean canAttack() {
        return false;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getRange() {
        return 0;
    }
}
