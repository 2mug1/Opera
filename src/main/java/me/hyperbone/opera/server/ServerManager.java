package me.hyperbone.opera.server;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {

    private final Map<String, Server> serverMap = new HashMap<>();

    public ServerManager(Plugin plugin) {}

    public void initOrUpdateServer(Server server) {
        if (this.serverMap.get(server.getServerId().toLowerCase()) != null) {
            this.serverMap.replace(server.getServerId().toLowerCase(), server);
            return;
        }

        this.serverMap.put(server.getServerId().toLowerCase(), server);
    }

    public Server getServerByName(String serverName) {
        return this.serverMap.getOrDefault(serverName.toLowerCase(), null);
    }

    public Map<String, Server> getServerMap() {
        return this.serverMap;
    }
}
