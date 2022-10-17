package me.hyperbone.opera.task;

import me.hyperbone.opera.OperaAPI;
import me.hyperbone.opera.server.Server;
import me.hyperbone.opera.server.ServerManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OnlinePlayerCountTask extends BukkitRunnable {

    private final ServerManager serverManager;

    public OnlinePlayerCountTask(Plugin plugin, ServerManager serverManager) {
        this.serverManager = serverManager;
        this.runTaskTimerAsynchronously(plugin, 0, 10);
    }

    @Override
    public void run() {
        int count = 0;

        for (Server server : serverManager.getServerMap().values()) {
            count = count + server.getOnlinePlayers();
        }

        OperaAPI.getInstance().setOnlinePlayers(count);
    }
}
