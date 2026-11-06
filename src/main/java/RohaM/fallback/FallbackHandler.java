package RohaM.fallback;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.network.connection.handler.IReconnectHandler;
import dev.waterdog.waterdogpe.network.connection.handler.ReconnectReason;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class FallbackHandler implements IReconnectHandler {

    private static final Set<String> DOWN_REASONS = Set.of("unknown", "timeout", "exception", "transfer_failed");
    private static final long NOTICE_TTL_MILLIS = 30_000L;
    private static final int NOTICE_PURGE_SIZE = 256;

    private record Notice(String target, String from, long createdAt) {
    }

    private final ProxyServer proxy;
    private final IReconnectHandler previous;
    private volatile FallbackConfig config;
    private volatile boolean active = true;

    private final Map<String, Long> downUntil = new ConcurrentHashMap<>();
    private final Map<UUID, Notice> notices = new ConcurrentHashMap<>();

    FallbackHandler(ProxyServer proxy, IReconnectHandler previous, FallbackConfig config) {
        this.proxy = proxy;
        this.previous = previous;
        this.config = config;
    }

    IReconnectHandler getPrevious() {
        return this.previous;
    }

    void deactivate() {
        this.active = false;
        this.downUntil.clear();
        this.notices.clear();
    }

    @Override
    public ServerInfo getFallbackServer(ProxiedPlayer player, ServerInfo oldServer, ReconnectReason reason, String kickMessage) {
        FallbackConfig cfg = this.config;
        if (!this.active || !cfg.enabled || !cfg.reasons.contains(reasonName(reason))) {
            return this.previous == null ? null : this.previous.getFallbackServer(player, oldServer, reason, kickMessage);
        }

        this.markDown(cfg, oldServer, reason);

        ServerInfo target = this.select(cfg, player, oldServer);
        if (target != null && !cfg.message.isEmpty()) {
            this.rememberNotice(player, target, oldServer);
        }
        return target;
    }

    @Override
    public ServerInfo getTransferFailureServer(ProxiedPlayer player, ServerInfo targetServer, ReconnectReason reason, String kickMessage) {
        FallbackConfig cfg = this.config;
        if (!this.active || !cfg.enabled) {
            return this.previous == null ? null : this.previous.getTransferFailureServer(player, targetServer, reason, kickMessage);
        }

        this.markDown(cfg, targetServer, reason);
        if (cfg.stayOnTransferFailure) {
            return null; 
        }
        return this.getFallbackServer(player, targetServer, reason, kickMessage);
    }

    private ServerInfo select(FallbackConfig cfg, ProxiedPlayer player, ServerInfo oldServer) {
        ServerInfo current = player.getServerInfo();

        ServerInfo found = this.firstUsable(cfg.servers, oldServer, current);
        if (found == null && cfg.useProxyPriorities) {
            found = this.firstUsable(this.proxy.getConfiguration().getPriorities(), oldServer, current);
        }
        return found;
    }

    private ServerInfo firstUsable(List<String> names, ServerInfo oldServer, ServerInfo current) {
        if (names == null) return null;
        for (String name : names) {
            ServerInfo info = this.proxy.getServerInfo(name);
            if (info == null || info == oldServer || info == current || this.isDown(info.getServerName())) {
                continue;
            }
            return info;
        }
        return null;
    }

    private void markDown(FallbackConfig cfg, ServerInfo server, ReconnectReason reason) {
        if (server == null || cfg.cooldownMillis <= 0 || !DOWN_REASONS.contains(reasonName(reason))) {
            return;
        }
        this.downUntil.put(server.getServerName(), System.currentTimeMillis() + cfg.cooldownMillis);
    }

    private boolean isDown(String serverName) {
        Long until = this.downUntil.get(serverName);
        if (until == null) return false;
        if (System.currentTimeMillis() >= until) {
            this.downUntil.remove(serverName, until);
            return false;
        }
        return true;
    }

    private void rememberNotice(ProxiedPlayer player, ServerInfo target, ServerInfo from) {
        long now = System.currentTimeMillis();
        if (this.notices.size() >= NOTICE_PURGE_SIZE) {
            this.notices.values().removeIf(n -> now - n.createdAt() > NOTICE_TTL_MILLIS);
        }
        this.notices.put(player.getUniqueId(),
                new Notice(target.getServerName(), from == null ? "?" : from.getServerName(), now));
    }

    void onConnected(ProxiedPlayer player, ServerInfo server) {
        if (!this.active || player == null || server == null) return;

        this.downUntil.remove(server.getServerName());

        Notice notice = this.notices.get(player.getUniqueId());
        if (notice == null) return;

        boolean expired = System.currentTimeMillis() - notice.createdAt() > NOTICE_TTL_MILLIS;
        if (expired) {
            this.notices.remove(player.getUniqueId(), notice);
            return;
        }
        if (!notice.target().equals(server.getServerName())) return;

        this.notices.remove(player.getUniqueId(), notice);
        if (!player.isConnected()) return;
        player.sendMessage(this.config.message
                .replace("{player}", player.getName())
                .replace("{from}", notice.from())
                .replace("{server}", notice.target()));
    }

    void onDisconnected(ProxiedPlayer player) {
        if (player != null) this.notices.remove(player.getUniqueId());
    }

    private static String reasonName(ReconnectReason reason) {
        String name = reason == null ? null : reason.getName();
        return name == null ? "unknown" : name.toLowerCase(Locale.ROOT);
    }
}
