package RohaM.fallback;

import dev.waterdog.waterdogpe.utils.config.Configuration;

import java.util.*;

final class FallbackConfig {

    private static final List<String> DEFAULT_REASONS = List.of(
            "unknown", "timeout", "exception", "server_kick", "transfer_failed", "incompatible");

    final boolean enabled;
    final List<String> servers;
    final boolean useProxyPriorities;
    final long cooldownMillis;
    final Set<String> reasons;
    final boolean stayOnTransferFailure;
    final String message;

    private FallbackConfig(boolean enabled, List<String> servers, boolean useProxyPriorities, long cooldownMillis,
                           Set<String> reasons, boolean stayOnTransferFailure, String message) {
        this.enabled = enabled;
        this.servers = servers;
        this.useProxyPriorities = useProxyPriorities;
        this.cooldownMillis = cooldownMillis;
        this.reasons = reasons;
        this.stayOnTransferFailure = stayOnTransferFailure;
        this.message = message;
    }

    static FallbackConfig load(Configuration c) {
        boolean enabled = bool(c.get("enabled"), true);
        List<String> servers = strings(c.get("servers"), List.of());
        boolean priorities = bool(c.get("use-proxy-priorities"), true);
        long cooldown = Math.max(0, number(c.get("down-cooldown-seconds"), 10)) * 1000L;

        Set<String> reasons = new HashSet<>();
        for (String reason : strings(c.get("reasons"), DEFAULT_REASONS)) {
            reasons.add(reason.toLowerCase(Locale.ROOT));
        }

        boolean stay = bool(c.get("stay-on-transfer-failure"), true);

        Object raw = c.get("messages.fallback");
        String message = raw == null ? "" : String.valueOf(raw).replace('&', '§');

        return new FallbackConfig(enabled, servers, priorities, cooldown, Set.copyOf(reasons), stay, message);
    }

    private static boolean bool(Object value, boolean def) {
        if (value instanceof Boolean b) return b;
        if (value instanceof String s) {
            String v = s.trim();
            if (v.equalsIgnoreCase("true")) return true;
            if (v.equalsIgnoreCase("false")) return false;
        }
        return def;
    }

    private static long number(Object value, long def) {
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) {
            try {
                return Long.parseLong(s.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return def;
    }

    private static List<String> strings(Object value, List<String> def) {
        if (value == null) return def;
        Collection<?> items = value instanceof Collection<?> col ? col : List.of(value);
        LinkedHashSet<String> out = new LinkedHashSet<>();
        for (Object item : items) {
            if (item == null) continue;
            String s = String.valueOf(item).trim();
            if (!s.isEmpty()) out.add(s);
        }
        return List.copyOf(out);
    }
}
