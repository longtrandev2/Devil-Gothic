package com.myteam.rpgsurvivor.animation;

import com.myteam.rpgsurvivor.model.enum_type.HeroType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class AnimationForSummondKnight {
    private static final float DEFAULT_FRAME_DURATION = 0.1f;

    public AnimationManager createMiniKnightAnimation(HeroType heroType)
    {
        AnimationManager animationManager = new AnimationManager();

        switch (heroType)
        {
            case SUMMON_KNIGHT:
                setUpSummonKnightAnimation(animationManager);
                break;
        }
        return animationManager;
    }

    private void setUpSummonKnightAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Skills/Knight Spawn/Idle.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Skills/Knight Spawn/Attack.png",
            6,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Skills/Knight Spawn/Walk.png",
            8,1, DEFAULT_FRAME_DURATION, true
        );

        animationManager.addAnimation(
            StateType.STATE_HURT.stateType,
            "Skills/Knight Spawn/Hurt.png",
            4,1, DEFAULT_FRAME_DURATION, true
        );
    }
}
