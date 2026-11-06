# Installation / نصب

## English
1. Stop the proxy.
2. Copy `Fallback.jar` into the `plugins` folder of the proxy.
3. Start the proxy. The folder `plugins/fallback/` and its `config.yml` are created automatically.
4. Make sure every name in `servers` exists in the proxy config (`servers` section / `listener.priorities`).
   Unknown names are ignored and a warning is printed to the console.
5. Restart the proxy after editing `config.yml`.

### Testing
1. Join the proxy and go to a game server.
2. Stop that server.
3. You should be moved to the first usable server of `servers` and receive the configured message.

### Uninstall
Remove `Fallback.jar`. The proxy's own reconnect handler (`listener.reconnect_handler`) is used again.

## فارسی
1. پروکسی رو خاموش کن.
2. فایل `Fallback.jar` رو بنداز تو پوشه `plugins` پروکسی.
3. پروکسی رو روشن کن. پوشه `plugins/fallback/` و فایل `config.yml` خودکار ساخته می‌شن.
4. مطمئن شو همه اسم‌های `servers` تو کانفیگ پروکسی وجود دارن. اسم‌های ناشناخته نادیده گرفته می‌شن و تو کنسول warning میاد.
5. بعد از تغییر `config.yml` پروکسی رو ریستارت کن.

### تست
1. وارد پروکسی شو و برو به یک سرور بازی.
2. اون سرور رو خاموش کن.
3. باید به اولین سرور قابل استفاده از `servers` منتقل بشی و پیام تنظیم‌شده رو ببینی.

### حذف
فایل `Fallback.jar` رو پاک کن. دوباره reconnect handler خود پروکسی (`listener.reconnect_handler`) استفاده می‌شه.
