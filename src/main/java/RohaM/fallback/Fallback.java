package RohaM.fallback;

import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerConnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.TransferCompleteEvent;
import dev.waterdog.waterdogpe.network.connection.handler.IReconnectHandler;
import dev.waterdog.waterdogpe.plugin.Plugin;

import java.util.List;

public final class Fallback extends Plugin {

    private FallbackHandler handler;

    @Override
    public void onEnable() {
        ProxyServer proxy = this.getProxy();
        FallbackConfig config = FallbackConfig.load(this.getConfig());

        this.validate(proxy, config);

        IReconnectHandler previous = proxy.getReconnectHandler();
        if (previous instanceof FallbackHandler old) {
            old.deactivate();
            previous = old.getPrevious();
        }

        FallbackHandler handler = new FallbackHandler(proxy, previous, config);
        this.handler = handler;
        proxy.setReconnectHandler(handler);

        proxy.getEventManager().subscribe(TransferCompleteEvent.class,
                event -> handler.onConnected(event.getPlayer(), event.getTargetServer()));
        proxy.getEventManager().subscribe(InitialServerConnectedEvent.class,
                event -> handler.onConnected(event.getPlayer(), event.getServerInfo()));
        proxy.getEventManager().subscribe(PlayerDisconnectedEvent.class,
                event -> handler.onDisconnected(event.getPlayer()));

        this.getLogger().info("Fallback enabled by RohaM -> " + (config.enabled ? config.servers : "disabled"));
    }

    @Override
    public void onDisable() {
        if (this.handler == null) return;
        ProxyServer proxy = this.getProxy();
        if (proxy.getReconnectHandler() == this.handler && this.handler.getPrevious() != null) {
            proxy.setReconnectHandler(this.handler.getPrevious());
        }
        this.handler.deactivate(); 
        this.handler = null;
    }

    private void validate(ProxyServer proxy, FallbackConfig config) {
        if (!config.enabled) return;

        boolean anyValid = false;
        for (String name : config.servers) {
            if (proxy.getServerInfo(name) == null) {
                this.getLogger().warn("Server '" + name + "' from config.yml is not defined in the proxy config, it will be ignored!");
            } else {
                anyValid = true;
            }
        }

        if (!anyValid) {
            List<String> priorities = proxy.getConfiguration().getPriorities();
            if (config.useProxyPriorities && priorities != null && !priorities.isEmpty()) {
                this.getLogger().warn("No valid fallback server in config.yml, the proxy priorities " + priorities + " will be used.");
            } else {
                this.getLogger().warn("No valid fallback server configured! Players will be kicked when their server goes down.");
            }
        }
    }
}
