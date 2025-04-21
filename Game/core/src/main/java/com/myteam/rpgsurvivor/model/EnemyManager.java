package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class EnemyManager {
    private Array<Enemy> enemies;
    private Player player; // Tham chiếu đến người chơi để truyền cho Enemy

    // --- Logic Spawn ---
    private float spawnInterval = 2f; // Spawn 1 enemy mỗi 2 giây
    private float timeSinceLastSpawn;
    private float spawnRadiusMin = 300f; // Khoảng cách tối thiểu từ player để spawn
    private float spawnRadiusMax = 500f; // Khoảng cách tối đa từ player để spawn

    // (Tùy chọn) Giới hạn số lượng enemy
    private int maxEnemies = 50;


    public EnemyManager(Player player) {
        this.player = player;
        this.enemies = new Array<>();
        this.timeSinceLastSpawn = 0f;
    }

    public void update(float delta) {
        // --- Cập nhật Spawn ---
        timeSinceLastSpawn += delta;
        if (timeSinceLastSpawn >= spawnInterval && enemies.size < maxEnemies) {
            spawnEnemy();
            timeSinceLastSpawn = 0f; // Reset bộ đếm
        }

        // --- Cập nhật từng Enemy ---
        // Sử dụng iterator để tránh ConcurrentModificationException nếu có xóa enemy trong lúc lặp
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta);
            // Thêm logic kiểm tra nếu enemy chết hoặc ra quá xa thì xóa khỏi list
            // if (enemy.isDead() || isTooFar(enemy)) {
            //     enemy.dispose(); // Giải phóng tài nguyên của enemy này
            //     enemies.removeIndex(i);
            // }
        }
    }

    private void spawnEnemy() {
        Vector2 playerPos = player.getPosition();
        Vector2 spawnPos = new Vector2();

        // Tạo vị trí spawn ngẫu nhiên xung quanh người chơi nhưng ngoài màn hình hoặc ở khoảng cách nhất định
        float angle = MathUtils.random(0f, 360f) * MathUtils.degreesToRadians; // Góc ngẫu nhiên
        float radius = MathUtils.random(spawnRadiusMin, spawnRadiusMax); // Khoảng cách ngẫu nhiên

        spawnPos.x = playerPos.x + MathUtils.cos(angle) * radius;
        spawnPos.y = playerPos.y + MathUtils.sin(angle) * radius;

        // (Tùy chọn) Kiểm tra xem vị trí spawn có hợp lệ không (ví dụ: nằm trong map)
        // if (!isValidSpawnPoint(spawnPos)) {
        //     return; // Hoặc thử lại vị trí khác
        // }

        Enemy newEnemy = new Enemy(spawnPos.x, spawnPos.y, player);
        enemies.add(newEnemy);
        Gdx.app.log("EnemyManager", "Spawned enemy at: " + spawnPos + ", Total: " + enemies.size);
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }
    }

    public void dispose() {
        // Giải phóng tài nguyên của tất cả enemies còn lại
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        enemies.clear();
        Gdx.app.log("EnemyManager", "Disposed.");
    }

    // Getter để các hệ thống khác (như xử lý va chạm) có thể truy cập danh sách enemy
    public Array<Enemy> getEnemies() {
        return enemies;
    }


    // (Ví dụ hàm kiểm tra vị trí spawn hợp lệ - cần điều chỉnh theo map của bạn)
    // private boolean isValidSpawnPoint(Vector2 point) {
    //    // Ví dụ: Kiểm tra xem có nằm trong giới hạn map không
    //    // return point.x >= 0 && point.x < mapWidth && point.y >= 0 && point.y < mapHeight;
    //    return true; // Tạm thời luôn hợp lệ
    // }

    // (Ví dụ hàm kiểm tra enemy có quá xa để xóa không)
    // private boolean isTooFar(Enemy enemy) {
    //    float maxDistance = 1000f; // Ví dụ: xóa enemy nếu cách player hơn 1000 pixel
    //    return enemy.getPosition().dst(player.getPosition()) > maxDistance;
    // }
}
