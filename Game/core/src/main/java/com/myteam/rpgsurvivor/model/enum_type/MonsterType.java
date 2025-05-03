package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum MonsterType {
    GOBLIN(new EntityStat(150, 15, 40, 1.8f, 20),EnemyHitbox.GOBLIN),
    SKELETON(new EntityStat(180, 20, 40, 1.7f, 20f), EnemyHitbox.RAT),
    RAT(new EntityStat(120, 30, 40, 2.2f, 15f), EnemyHitbox.SKELETON);
//    STONE_GOLEM(new EntityStat(250, 35, 40, 1.5f, 10f));

    public final EntityStat stat;
    public final EnemyHitbox hitbox;

    MonsterType(EntityStat stat, EnemyHitbox hitbox) {

        this.stat = stat;
        this.hitbox = hitbox;
    }
}
