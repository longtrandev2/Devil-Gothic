package com.myteam.rpgsurvivor.model;

public class EntityStat {
    public int maxHealth;
    public int damage;
    public float moveSpeed;
    public float attackSpeed;
    public float rangeAttack;

    public EntityStat(int maxHealth, int damage, float moveSpeed, float attackSpeed, float rangeAttack) {
        this.maxHealth = maxHealth;
        this.damage = damage;
        this.moveSpeed = moveSpeed;
        this.attackSpeed = attackSpeed;
        this.rangeAttack = rangeAttack;
    }
}
