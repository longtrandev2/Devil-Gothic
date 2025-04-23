package com.myteam.rpgsurvivor.model.enum_type;

import com.myteam.rpgsurvivor.model.EntityStat;

public enum HeroType {
    KNIGHT(new EntityStat(250, 18, 50f, 1.5f, 1.0f)),
    SAMURAI(new EntityStat(180, 23, 125f, 2.2f, 1.5f)),
    WIZARD(new EntityStat(120, 40, 100f, 2.7f, 3.0f)),
    ARCHER(new EntityStat(130, 28, 140f, 2.8f, 3.5f)),
    SUMMON_KNIGHT(new EntityStat(100, 10, 1.5f, 1.5f, 1.0f));
    public final EntityStat stat;

    HeroType(EntityStat stat) {
        this.stat = stat;
    }
}
