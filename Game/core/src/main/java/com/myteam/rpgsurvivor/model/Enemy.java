    package com.myteam.rpgsurvivor.model;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.BitmapFont;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
    import com.badlogic.gdx.math.Circle;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector2;

    import com.badlogic.gdx.scenes.scene2d.ui.Label;
    import com.myteam.rpgsurvivor.animation.AnimationForEnemy;
    import com.myteam.rpgsurvivor.animation.AnimationManager;
    import com.myteam.rpgsurvivor.controller.movement.EnemyMovement;
    import com.myteam.rpgsurvivor.debug.DebugRenderer;
    import com.myteam.rpgsurvivor.model.enum_type.BossType;
    import com.myteam.rpgsurvivor.model.enum_type.HeroType;
    import com.myteam.rpgsurvivor.model.enum_type.MonsterType;
    import com.myteam.rpgsurvivor.model.enum_type.StateType;

    public abstract class Enemy extends Entity {
        // Stat
        protected int level;
        protected float attackCooldown;
        protected float attackTimer;
        protected Player targetPlayer;

        protected boolean isInvisible;
        protected boolean isInvulnerable;
        protected boolean isInteracting;


        protected float detectionRange;
        protected float attackRange;

        private float offsetX;
        private float offsetY;

        private StateType currentState;
        private EnemyMovement enemyMovement;
        private ShapeRenderer shapeRenderer;
        private Rectangle hitboxPlayer;
        private Rectangle hitboxEnemy;


        private Vector2 velocity = new Vector2();
        private float knockbackDecay = 10f;
        private float maxKnockbackSpeed = 300f;

        private boolean bossTurn;

        private Texture frameHP;
        private Texture fullBloodFrame ;

        private Texture bossName;
        private TextureRegion currentBloodFrame;

        private static final int BLOOD_FRAME_COLS = 6;
        private static final int BLOOD_FRAME_ROWS = 1;


        private float bloodBarWidth = 840;
        private float bloodBarHeight = 122;
        private float padding = 100;
        private float bloodBarInnerPaddingX = 28;
        private float bloodBarInnerPaddingY = 50;

        private float innerBloodWidth = 795;
        private float innerBloodHeight = 25;
        private float bloodBarX;
        private float bloodBarY;


        public Enemy(float x, float y, MonsterType enemyType, Player player, AnimationForEnemy animationFactory) {
            this.entityX = x;
            this.entityY = y;

            // Stat
            this.stat = enemyType.stat;
            this.level = 1;
            this.currentHealth = stat.maxHealth;

            // Khởi tạo hitbox
            this.hitbox = enemyType.hitbox.createHitbox(entityX,entityY);
            offsetX = enemyType.hitbox.getOffsetX();
            offsetY = enemyType.hitbox.getOffsetY();


            // Thiết lập player là mục tiêu
            this.targetPlayer = player;

            entityX = x;
            entityY = y;
            hitboxPlayer = targetPlayer.getHitbox();
            hitboxEnemy = getHitBox();
            this.enemyMovement = new EnemyMovement(x,y,player,enemyType.stat.moveSpeed);
            this.animationManager = animationFactory.createEnemyAnimation(enemyType);
            this.currentState = StateType.STATE_IDLE;


            this.attackCooldown = 1f / stat.attackSpeed;
            this.attackTimer = 0;
            this.attackRange = stat.rangeAttack;
            this.detectionRange = 1000f;

            this.movement = new EnemyMovement(x,y, player, stat.moveSpeed);

            this.isInvisible = false;
            this.isInvulnerable = false;
            this.isInteracting = false;
            this.isAttack = false;

            this.bossTurn = false;

            this.attackbox = new Rectangle(hitbox);
            attackbox.setSize(hitbox.getWidth() + attackRange , hitbox.getHeight() + attackRange);
        }

        public Enemy(float x, float y, BossType bossType, Player player, AnimationForEnemy animationFactory) {
            this.entityX = x;
            this.entityY = y;

            // Stat
            this.stat = bossType.stat;
            this.level = 1;
            this.currentHealth = stat.maxHealth;

            // Khởi tạo hitbox
            this.hitbox = bossType.hitbox.createHitbox(entityX,entityY);
            offsetX = bossType.hitbox.getOffsetX();
            offsetY = bossType.hitbox.getOffsetY();


            // Thiết lập player là mục tiêu
            this.targetPlayer = player;

            entityX = x;
            entityY = y;
            hitboxPlayer = targetPlayer.getHitbox();
            hitboxEnemy = getHitBox();
            this.enemyMovement = new EnemyMovement(x,y,player,bossType.stat.moveSpeed);
            this.animationManager = animationFactory.createBossAnimation(bossType);
            this.currentState = StateType.STATE_IDLE;


            this.attackCooldown = 1f / stat.attackSpeed;
            this.attackTimer = 0;
            this.attackRange = stat.rangeAttack;
            this.detectionRange = 1000f;

            this.movement = new EnemyMovement(hitboxEnemy.x, hitboxEnemy.y, player, stat.moveSpeed);

            this.isInvisible = false;
            this.isInvulnerable = false;
            this.isInteracting = false;
            this.isAttack = false;

            this.bossTurn = true;

            // Khởi tạo textures cho thanh máu của boss
            frameHP = new Texture(Gdx.files.internal("Enemy/Asset For Boss/FrameHP.png"));
            fullBloodFrame = new Texture(Gdx.files.internal("Enemy/Asset For Boss/FullBloodFrame.png"));

            // Khởi tạo kích thước thanh máu
            innerBloodWidth = bloodBarWidth - (2 * bloodBarInnerPaddingX) + 30;
            innerBloodHeight = bloodBarHeight - (2 * bloodBarInnerPaddingY) ;
            bloodBarX = padding + padding;
            bloodBarY = bloodBarHeight / 2;

            // Thiết lập frame cho máu
            setupBloodFrames();

            this.attackbox = new Rectangle(hitbox);
            attackbox.setSize(hitbox.getWidth() + attackRange , hitbox.getHeight() + attackRange);

            if(BossType.SLIME_BOSS.name().equals(bossType.name()))
            {
                bossName = new Texture(Gdx.files.internal("Enemy/Asset For Boss/SlimebossName.png"));
            }
            else if(BossType.SKELETON_KING.name().equals(bossType.name()))
            {
                bossName = new Texture(Gdx.files.internal("Enemy/Asset For Boss/Igris.png"));
            }
        }

        private void setupBloodFrames() {
            currentBloodFrame = new TextureRegion();
            float frameWidth = fullBloodFrame.getWidth();
            float frameHeight = fullBloodFrame.getHeight();

            currentBloodFrame = new TextureRegion(fullBloodFrame, 0 , 0, (int)frameWidth, (int)frameHeight);
        }



        private void updateHealth(float health) {
            currentHealth = (int) Math.max(0, Math.min(health, getMaxHealth()));
        }

        @Override
        public void update(float deltaTime) {
            updateHealth(getCurrentHealth());
            // Cập nhật vị trí hitbox
            if (!velocity.isZero()) {
                entityX += velocity.x * deltaTime;
                entityY += velocity.y * deltaTime;

                float decay = knockbackDecay * deltaTime;
                if (velocity.len() <= decay) {
                    velocity.setZero();
                } else {
                    velocity.scl(1 - decay / velocity.len());
                }
            }

            hitbox.setPosition(entityX + offsetX, entityY + offsetY);

        if (isDead)
        {
            animationManager.setState(StateType.STATE_DEATH.stateType, true);
            return;
        }

            // Cập nhật vị trí attackbox
            if(isFacingRight()){
                attackbox.setPosition(entityX + offsetX , entityY + offsetY);
            } else {
                attackbox.setPosition(entityX + offsetX - attackRange, entityY + offsetY);
            }

            if (isDead) return;

            if (isHurt) {
                hurtTimer -= deltaTime;
                if (hurtTimer <= 0) {
                    isHurt = false;
                } else {
                    currentState = StateType.STATE_HURT;
                    animationManager.setState(StateType.STATE_HURT.stateType, true);
                    return;
                }
            }

            // Kiểm tra player có trong tầm phát hiện không
            if (isPlayerInDetectionRange()) {
                if (isPlayerInAttackRange()) {
                    tryAttack();
                }
            }

            if (attackTimer > 0) {
                attackTimer -= deltaTime;
            }

            float dx = 0;
            // Tính hiệu dx nhỏ nhất
            if(hitboxEnemy.getX() + hitboxEnemy.getWidth() < hitboxPlayer.getX())
                dx = hitboxPlayer.getX() - (hitboxEnemy.getX() + hitboxEnemy.getWidth());
            else if(hitboxPlayer.getX() + hitboxPlayer.getWidth() < hitboxEnemy.getX())
                dx = hitboxEnemy.getX() - (hitboxPlayer.getX() + hitboxPlayer.getWidth());

            float dy = 0;
            // Tính hiệu dy nhỏ nhất
            if(hitboxEnemy.getY() + hitboxEnemy.getHeight() < hitboxPlayer.getY())
                dy = hitboxPlayer.getY() - (hitboxEnemy.getY() + hitboxEnemy.getHeight());
            else if(hitboxPlayer.getY() + hitboxPlayer.getHeight() < hitboxEnemy.getY())
                dy = hitboxEnemy.getY() - (hitboxPlayer.getY() + hitboxPlayer.getHeight());

            float shortestDistanceToPlayer = (float) Math.sqrt(dx * dx + dy * dy);

            if (shortestDistanceToPlayer <= detectionRange && shortestDistanceToPlayer > 0) {
                currentState = StateType.STATE_RUN;
                // Sử dụng deltaTime được truyền vào
                Vector2 newDirection = enemyMovement.move(deltaTime);
                entityX = newDirection.x;
                entityY = newDirection.y;
            }
            else if (isPlayerInAttackRange()) {
                if (isAttack) {
                    currentState = StateType.STATE_ATTACK;
                } else {
                    currentState = StateType.STATE_IDLE;
                }
            } else {
                currentState = StateType.STATE_IDLE;
            }

            if (targetPlayer.getHitbox().x > hitboxEnemy.getX() ) {
                facingRight = true;
            }
            else {
                facingRight = false;
            }

            if (animationManager != null) {
                animationManager.setState(currentState.stateType, true);

                if (isAttack && animationManager.isAnimationFinished()) {
                    isAttack = false;
                }
            }
        }

        @Override
        public void render(SpriteBatch batch, float deltaTime) {


//        if (isDead || animationManager == null) {
//            return;
//        }



            update(deltaTime);

            animationManager.update(deltaTime);


        if (isDead)
        {
                batch.draw(animationManager.getCurrentFrame(), entityX, entityY);
                return;
        }


            // Thiết lập hướng di chuyển
            this.setFacingRight(facingRight);
            animationManager.setFacingRight(facingRight);

            TextureRegion tr  = animationManager.getCurrentFrame();
            Rectangle hitboxFrame = new Rectangle(entityX, entityY, tr.getRegionWidth(), tr.getRegionHeight());
            DebugRenderer.drawRect(hitboxFrame, Color.GREEN);
            batch.draw(animationManager.getCurrentFrame(), entityX, entityY);

            if (bossTurn) {

                float healthPercent = currentHealth / (float) getMaxHealth();
                float currentBloodWidth = innerBloodWidth * healthPercent;
                // Vẽ khung thanh máu
                batch.draw(frameHP,
                    bloodBarX,
                    bloodBarY);
                int newWidth = (int)(fullBloodFrame.getWidth() * healthPercent);
                int height = fullBloodFrame.getHeight();
                currentBloodFrame = new TextureRegion(fullBloodFrame, 0, 0, newWidth, height);
                batch.draw(
                    currentBloodFrame,
                    bloodBarX + bloodBarInnerPaddingX,
                    bloodBarY + bloodBarInnerPaddingY
                );

                batch.draw(bossName,bloodBarX, bloodBarY + 100);
            }

            // Debug drawing
            DebugRenderer.drawRect(hitboxPlayer, Color.GREEN);
            DebugRenderer.drawRect(getHitBox(), Color.YELLOW);
            DebugRenderer.drawRect(attackbox, Color.RED);

        }

        public boolean isPlayerInDetectionRange() {
            float distance = Vector2.dst(entityX, entityY, targetPlayer.getHitbox().getX(), targetPlayer.getHitbox().getY());
            return distance <= detectionRange;
        }

        public boolean isPlayerInAttackRange() {
            return attackbox.overlaps(targetPlayer.hitbox);
        }

        private void tryAttack() {
            if (attackTimer <= 0 && !isAttack) {
                performAttack();
                attackTimer = attackCooldown;
            }
        }

        protected void performAttack() {
            isAttack = true;
            if (animationManager != null) {
                animationManager.setState(StateType.STATE_ATTACK.stateType, true);
            }

            if(currentState == StateType.STATE_ATTACK) {
                targetPlayer.takeDamge(getDamage());
                targetPlayer.onHurt();
            }
        }

        public void setDetectionRange(float range) {
            this.detectionRange = range;
        }

        public void setAttackRange(float range) {
            this.attackRange = range;
        }

        public Rectangle getHitBox() {
            return hitbox;
        }

        public void onHurt() {
            if(!bossTurn) {
                isHurt = true;
                hurtTimer = 0.4f;
                animationManager.setState(StateType.STATE_HURT.stateType, true);
            }
        }

        public void onDeath() {
    //        if(is)
        }

        public void applyExternalForce(float dx, float dy) {
            this.entityX+= dx;
            this.entityY += dy;
            enemyMovement.updateEnemyPosition(this.entityX, this.entityY);
        }


        public boolean isBoss(){
            return  bossName != null;
        }

        public void applyDifficulty(int difficulty) {
            float scale = 1f + 0.05f * difficulty;
            this.stat.maxHealth *= scale;
            this.stat.damage *= scale;
            this.stat.moveSpeed *= 1f + 0.01f * difficulty;
        }
    }

