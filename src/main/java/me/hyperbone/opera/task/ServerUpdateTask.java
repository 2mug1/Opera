package me.hyperbone.opera.task;

import me.hyperbone.hakobi.hakobi.letter.Letter;
import me.hyperbone.opera.Opera;
import me.hyperbone.opera.OperaConfig;
import me.hyperbone.opera.server.Server;
import me.hyperbone.opera.server.ServerManager;
import me.hyperbone.opera.server.ServerStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ServerUpdateTask extends BukkitRunnable {

    private final ServerManager serverManager;
    private final String serverName;
    private final String bungeeName;

    public ServerUpdateTask(Plugin plugin, ServerManager serverManager) {
        this.serverManager = serverManager;

        YamlConfiguration yaml = OperaConfig.loadYamlConfig();
        this.serverName = yaml.getString("server.id");
        this.bungeeName = yaml.getString("server.bungee");

        this.runTaskTimer(plugin, 0, yaml.getLong("server.refresh_time") * 20);
    }

    @Override
    public void run() {
        Map<String, String> data = new HashMap<>();
        Server server = this.serverManager.getServerByName(this.serverName.toLowerCase());

        if (server == null || Opera.isSetup()) {
            Server serverToUpdate = new Server(this.serverName, this.bungeeName);

            data.put("serverId", serverToUpdate.getServerId());
            data.put("bungee", serverToUpdate.getBungee());
            data.put("onlinePlayers", String.valueOf(serverToUpdate.getOnlinePlayers()));
            data.put("maxPlayers", String.valueOf(serverToUpdate.getMaxPlayers()));
            data.put("serverStatus", ServerStatus.BOOTING.toString());
            data.put("serverVersion", serverToUpdate.getServerVersion());
        } else {
            data.put("serverId", server.getServerId());
            data.put("bungee", server.getBungee());
            data.put("onlinePlayers", String.valueOf(Bukkit.getOnlinePlayers().size()));
            data.put("maxPlayers", String.valueOf(Bukkit.getMaxPlayers()));
            data.put("serverStatus", Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED.toString() : ServerStatus.ONLINE.toString());
            data.put("serverVersion", server.getServerVersion());
        }

        Letter letter = new Letter("update", data);
        Opera.getInstance().getJedisManager().getHakobi().sendLetter(letter);
    }
}
