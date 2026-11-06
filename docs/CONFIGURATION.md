# Configuration / تنظیمات

File: `plugins/fallback/config.yml`

| Key | Default | Description / توضیح |
|-----|---------|---------------------|
| `enabled` | `true` | `false` = plugin does nothing, proxy's own handler is used / غیرفعال کردن پلاگین |
| `servers` | `[lobby1]` | Fallback servers in priority order. Names must match the proxy's servers / لیست سرورهای فال‌بک به ترتیب |
| `use-proxy-priorities` | `true` | If no server in `servers` is usable, try `listener.priorities` of the proxy / استفاده از priorities پروکسی |
| `down-cooldown-seconds` | `10` | Seconds a failed server is skipped. `0` disables / چند ثانیه سرور آف‌شده رد بشه |
| `reasons` | all | Disconnect reasons that trigger the fallback / دلایل فعال‌کننده فال‌بک |
| `stay-on-transfer-failure` | `true` | Player on a healthy server stays there when the target server is offline / ماندن پلیر روی سرور سالم |
| `messages.fallback` | see file | Message after the move. Empty = none. Placeholders: `{player}` `{from}` `{server}`. Colors with `&` / پیام بعد از انتقال |

## Reasons / دلیل‌ها
`unknown`, `timeout`, `exception`, `server_kick`, `transfer_failed`, `incompatible`

- `unknown` – connection closed silently (server stopped / crashed)
- `timeout` – server stopped answering
- `exception` – network error
- `server_kick` – the server kicked the player (also sent by PocketMine on shutdown, but also on bans/whitelist)
- `transfer_failed` – connecting to a server failed
- `incompatible` – server settings incompatible

Only `unknown`, `timeout`, `exception` and `transfer_failed` mark a server as offline for `down-cooldown-seconds`.
A kick alone does not, because it can be a ban or whitelist message and not a real outage.

## How the target is chosen / انتخاب سرور مقصد
1. Servers from `servers`, in order.
2. If `use-proxy-priorities` is `true`: servers from the proxy's `listener.priorities`.
3. If nothing is usable the player is disconnected by the proxy.

A server is skipped if it does not exist in the proxy, is the server that just failed, is the player's current server, or is marked offline.

## Example / نمونه
```yaml
enabled: true
servers:
  - lobby1
  - lobby2
use-proxy-priorities: true
down-cooldown-seconds: 10
reasons: [unknown, timeout, exception, server_kick, transfer_failed, incompatible]
stay-on-transfer-failure: true
messages:
  fallback: "&eThe server &c{from} &eis offline. You were moved to &a{server}&e."
```
