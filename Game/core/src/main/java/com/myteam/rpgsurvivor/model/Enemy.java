package com.myteam.rpgsurvivor.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    private Vector2 position;
    private float speed = 50f; // Tốc độ di chuyển của Enemy (pixel/giây)
    private Player target; // Tham chiếu đến người chơi
    // Thêm các thuộc tính khác: máu, hình ảnh, animation...

    public Enemy(float x, float y, Player target) {
        this.position = new Vector2(x, y);
        this.target = target;
        // Khởi tạo hình ảnh, animation...
    }

    public void update(float delta) {
        if (target != null) {
            // 1. Tính toán hướng tới người chơi
            Vector2 targetPosition = target.getPosition(); // Giả sử Player có hàm getPosition() trả về Vector2
            Vector2 direction = targetPosition.cpy().sub(this.position).nor(); // Vector chỉ hướng từ Enemy tới Player

            // 2. Di chuyển Enemy theo hướng đó
            position.mulAdd(direction, speed * delta); // position += direction * speed * delta

            // Cập nhật animation (nếu có)
        }
        // Cập nhật logic khác của Enemy (ví dụ: kiểm tra va chạm)
    }

    public void render(SpriteBatch batch) {
        // Vẽ Enemy lên màn hình tại vị trí 'position'
        // Ví dụ: batch.draw(enemyTexture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    // Thêm các getter/setter hoặc phương thức cần thiết khác
    public void dispose() {
        // Giải phóng tài nguyên (texture, sound...)
    }
}
