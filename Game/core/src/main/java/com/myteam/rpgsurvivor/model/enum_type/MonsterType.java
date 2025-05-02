package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum MonsterType {
    GOBLIN(new EntityStat(150, 15, 40, 1.8f, 20)),
    SKELETON(new EntityStat(180, 20, 40, 1.7f, 20f)),
    RAT(new EntityStat(120, 30, 40, 2.2f, 15f)),
    STONE_GOLEM(new EntityStat(250, 35, 40, 1.5f, 10f));

    public final EntityStat stat;

    MonsterType(EntityStat stat) {
        this.stat = stat;
    }
}
