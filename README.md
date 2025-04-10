# Game Design Document – RPG + Survival (Inspired by *Vampire Survivors*)

## 🎯 Mục Tiêu
- Sinh tồn càng lâu càng tốt
- Tiêu diệt quái, nâng cấp nhân vật

---

## 🎮 Điều Khiển
- **WASD** – Di chuyển  
- **J** – Đánh thường (auto hoặc nhấn)  
- **K** – Dùng skill nhân vật (khác nhau tùy nhân vật)

---

## 🌍 Bản Đồ
- 1 bản đồ duy nhất, rộng
- Quái spawn liên tục xung quanh người chơi

---

## 🧮 Tính Điểm
- Tính theo **thời gian sống sót**

---

## 👾 Hệ Thống Quái & Boss

### Quái Thường
- Spawn liên tục
- Càng về sau càng đông và mạnh hơn

### Boss
- Mỗi **5 round thường** sẽ gặp **1 round Boss**
- Boss có kỹ năng riêng, mạnh hơn quái thường
- Về sau có thể gặp **2 Boss cùng lúc**

---

## 🧙‍♂️ Hệ Thống Nhân Vật & Kỹ Năng

### Chỉ Số Cơ Bản

| Nhân Vật | HP | Damage | Speed | Attack Speed | Ghi Chú |
|----------|----|--------|-------|--------------|---------|
| Knight   | x  | x      | x     | x            | Trâu, chậm, đánh đều, chơi an toàn |
| Wizard   | x  | x (AOE)| x     | x            | Yếu máu, skill mạnh, chơi chiến thuật |
| Yodle    | x  | x      | x     | x            | Cơ động, damage cao, kiểu sát thủ |
| Samurai  | x  | x      | x     | x            | Cân bằng, thiên về tốc độ đánh |

---

### Kỹ Năng Nhân Vật

| Nhân Vật | Default Skill | Cooldown | Gợi ý nâng cấp sau boss round |
|----------|----------------|-----------|-------------------------------|
| Knight   | **Summon** – Gọi 1 đệ cận chiến giúp tấn công | 10s | - Gọi thêm 1 đệ  <br> - Tăng damage đệ  <br> - Tăng máu đệ  <br> - Giảm cooldown |
| Wizard   | **Explosion** – Gây nổ AOE quanh người        | 8s  | - Tăng phạm vi nổ  <br> - Tăng damage  <br> - Giảm cooldown  <br> - Nổ 2 lần liên tiếp |
| Yodle    | **Steal** – Dịch chuyển tới trước + đâm mạnh  | 6s  | - Gây sát thương lan  <br> - Tăng damage  <br> - Giảm cooldown  <br> - Tăng tầm Tele |
| Samurai  | **Death Slash** – Chém nhanh theo hướng đi    | 7s  | - Chém 2 lần  <br> - Tăng damage  <br> - Giảm cooldown  <br> - Mở rộng góc chém |

---

## 📈 Nâng Cấp Nhân Vật

### Sau mỗi **Round Thường**:
Chọn **1 nâng cấp chỉ số**:
- Tăng Máu
- Tăng Tốc độ chạy
- Tăng Sát thương đánh thường
- Tăng Tốc độ đánh
- Hồi máu nhẹ

### Sau mỗi **Round Boss**:
Chọn **1 nâng cấp kỹ năng**

#### Cooldown Skill – Giới hạn nâng cấp

| Kỹ Năng               | Cooldown Gốc | Cooldown Tối Thiểu |
|-----------------------|---------------|----------------------|
| Gọi đệ (Knight)       | 10s           | 6s                   |
| Nổ AOE (Wizard)       | 8s            | 4.5s                 |
| Tele + đâm mạnh (Yodle)| 6s           | 3.5s                 |
| Slash nhanh (Samurai) | 7s            | 4s                   |
