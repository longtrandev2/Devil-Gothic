package com.myteam.rpgsurvivor.animation;

import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
import com.myteam.rpgsurvivor.model.enum_type.StateType;

public class AnimationForEnemy {
    private static final float DEFAULT_FRAME_DURATION = 0.1f;

    public AnimationManager createEnemyAnimation(MonsterType monsterType)
    {
        AnimationManager animationManager = new AnimationManager();

        switch (monsterType)
        {
            case GOBLIN:
                setUpGoblinAnimation(animationManager);
                break;
            case SKELETON:
                setUpSkeletonAnimation(animationManager);
                break;
            case RAT:
                setUpRatAnimation(animationManager);
                break;

        }
        return animationManager;
    }

    private void setUpGoblinAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_idle.png",
            4,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_attack.png",
            8,1,DEFAULT_FRAME_DURATION,false
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 1/HobgoblinNoOutline/goblinsmasher_run.png",
            4,1, DEFAULT_FRAME_DURATION, true
        );

    }

    public void setUpSkeletonAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Idle.png",
            11,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Walk.png",
            13,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Skeleton/Skeleton/Sprite Sheets/Skeleton Attack.png",
            18,1, DEFAULT_FRAME_DURATION,false
        );
    }

    public void setUpRatAnimation(AnimationManager animationManager)
    {
        animationManager.addAnimation(
            StateType.STATE_IDLE.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-idle.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_RUN.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-run.png",
            6,1,DEFAULT_FRAME_DURATION,true
        );

        animationManager.addAnimation(
            StateType.STATE_ATTACK.stateType,
            "Enemy/Creep 2/NoneOutlinedRat/rat-attack.png",
            6,1,DEFAULT_FRAME_DURATION,false
        );
    }

}
