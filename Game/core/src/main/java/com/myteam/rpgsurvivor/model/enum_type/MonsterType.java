package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum MonsterType {
    GOBLIN(new EntityStat(35, 18, 20, 1.8f, 10f),EnemyHitbox.GOBLIN),
    SKELETON(new EntityStat(40, 25, 20, 2.2f, 8f), EnemyHitbox.SKELETON),
    RAT(new EntityStat(30, 15, 21, 2.2f, 12f), EnemyHitbox.RAT),
    VAMPIRE(new EntityStat(50, 10, 20,2.2f, 10f), EnemyHitbox.VAMPIRE);
    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    MonsterType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}

