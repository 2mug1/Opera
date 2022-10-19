package me.hyperbone.opera.task;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.OperaConfig;
import me.hyperbone.opera.server.Server;
import me.hyperbone.opera.server.ServerManager;
import net.iamtakagi.iroha.Style;
import org.bukkit.Bukkit;
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
        for (Server server : this.serverManager.getServerMap().values()) {
            if ((System.currentTimeMillis() - server.getLastUpdate()) > (OperaConfig.loadYamlConfig().getInt("server.refresh_time") * 1000 * 2.5)) {
                Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("opera.log")).forEach(
                        player -> player.sendMessage(Opera.getInstance().getPrefix() +
                                Style.WHITE + Style.BOLD + server.getServerId() + Style.RESET + Style.RED + "が応答しないため、削除しました。")
                );
                this.serverManager.getServerMap().values().remove(server);
            }
        }
        //this.serverManager.getServerMap().values().removeIf(server -> (System.currentTimeMillis() - server.getLastUpdate()) > 5000);
    }
}
