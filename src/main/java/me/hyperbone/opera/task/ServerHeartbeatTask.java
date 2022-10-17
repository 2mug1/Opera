package me.hyperbone.opera.task;

import me.hyperbone.opera.OperaConfig;
import me.hyperbone.opera.server.ServerManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerHeartbeatTask extends BukkitRunnable {

    private final ServerManager serverManager;

    public ServerHeartbeatTask(Plugin plugin, ServerManager serverManager) {
        this.serverManager = serverManager;
        YamlConfiguration yaml = OperaConfig.loadYamlConfig();
        this.runTaskTimerAsynchronously(plugin, 0, yaml.getLong("server.refresh_time") * 20);
    }

    @Override
    public void run() {
        this.serverManager.getServerMap().values().removeIf(server -> (System.currentTimeMillis() - server.getLastUpdate()) > 5000);
    }
}
