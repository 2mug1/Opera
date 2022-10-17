package me.hyperbone.opera;

import me.hyperbone.opera.server.Server;
import me.hyperbone.opera.server.ServerManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;

public class OperaAPI {

    @Getter
    private static OperaAPI instance;
    private final ServerManager serverManager;
    @Getter
    @Setter
    private int onlinePlayers = 0;

    public OperaAPI(Plugin plugin, ServerManager serverManager) {
        instance = this;
        this.serverManager = serverManager;
    }

    public Server getServerByName(String server) {
        return this.serverManager.getServerByName(server.toLowerCase());
    }
}
