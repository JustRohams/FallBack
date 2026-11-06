<div align="center">

# Fallback

**Automatic fallback plugin for WaterdogPE**

Author: **RohaM** · [GitHub](https://github.com/JustRohams) · [Repository](https://github.com/JustRohams/FallBack)

[English](#english) · [فارسی](#فارسی)

</div>

---

## English

When a server goes offline, **Fallback** moves the players that were on it to a server you choose in `config.yml`
instead of kicking them.

### Features
- Works for every kind of failure: crash, timeout, connection error, kick, failed transfer.
- Ordered list of fallback servers, plus optional use of the proxy's own `listener.priorities`.
- Remembers servers that just failed (`down-cooldown-seconds`) so players are never sent to a dead server and no loops happen.
- Choose which disconnect reasons trigger the fallback.
- Configurable message sent after the player was moved.
- Lightweight and lock-free: no tasks, no disk I/O while running.
- Validates the config against the proxy's server list on startup.

### Installation
1. Download `Fallback.jar` from the [releases](https://github.com/JustRohams/FallBack/releases) or [build it](docs/BUILDING.md).
2. Put it in the proxy's `plugins` folder.
3. Start the proxy. `plugins/fallback/config.yml` is generated.
4. Set `servers` to the names of your lobby servers (they must exist in the proxy config) and restart.

See [docs/INSTALLATION.md](docs/INSTALLATION.md) and [docs/CONFIGURATION.md](docs/CONFIGURATION.md).

### Quick config
```yaml
enabled: true
servers:
  - lobby1
use-proxy-priorities: true
down-cooldown-seconds: 10
```

### Requirements
- WaterdogPE 2.x (Java 17+)

### License
[MIT](LICENSE) © 2026 RohaM

---

## فارسی

وقتی یک سرور آفلاین بشه، **Fallback** به‌جای کیک کردن، پلیرهای داخلش رو به سروری که تو `config.yml` تنظیم کردی منتقل می‌کنه.

### ویژگی‌ها
- روی همه نوع قطعی کار می‌کنه: کرش، تایم‌اوت، خطای اتصال، کیک و ترنسفر ناموفق.
- لیست سرورهای فال‌بک به ترتیب اولویت، به‌علاوه امکان استفاده از `listener.priorities` خود پروکسی.
- سرورهایی که تازه آف شدن رو یادش می‌مونه (`down-cooldown-seconds`) تا پلیر به سرور مرده منتقل نشه و لوپ نشه.
- انتخاب اینکه کدوم دلیل‌های قطعی فال‌بک رو فعال کنن.
- پیام قابل تنظیم بعد از انتقال پلیر.
- سبک و بدون قفل: بدون تسک و بدون I/O در زمان اجرا.
- بررسی کانفیگ با لیست سرورهای پروکسی هنگام استارت.

### نصب
1. فایل `Fallback.jar` رو از [releases](https://github.com/JustRohams/FallBack/releases) دانلود کن یا [خودت بیلد کن](docs/BUILDING.md).
2. بنداز تو پوشه `plugins` پروکسی.
3. پروکسی رو روشن کن تا فایل `plugins/fallback/config.yml` ساخته بشه.
4. مقدار `servers` رو با اسم سرورهای لابی خودت (که تو کانفیگ پروکسی هست) عوض کن و ریستارت کن.

جزئیات: [docs/INSTALLATION.md](docs/INSTALLATION.md) و [docs/CONFIGURATION.md](docs/CONFIGURATION.md).

### نیازمندی‌ها
- WaterdogPE نسخه 2 (جاوا 17 به بالا)

### لایسنس
[MIT](LICENSE) © 2026 RohaM
