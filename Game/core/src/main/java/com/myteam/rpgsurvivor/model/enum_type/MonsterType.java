package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum MonsterType {
    GOBLIN(new EntityStat(35, 18, 20, 1.8f, 10f),EnemyHitbox.GOBLIN),
    SKELETON(new EntityStat(45, 25, 20, 1.9f, 8f), EnemyHitbox.SKELETON),
    RAT(new EntityStat(30, 15, 22, 2.2f, 12f), EnemyHitbox.RAT),
    VAMPIRE(new EntityStat(60, 10, 20,1.9f, 10f), EnemyHitbox.VAMPIRE);
//    STONE_GOLEM(new EntityStat(250, 35, 40, 1.5f, 10f));

    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    MonsterType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}
