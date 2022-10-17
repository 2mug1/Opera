package me.hyperbone.opera.server;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class Server {

    private final String serverId;
    private final String bungee;
    private int onlinePlayers;
    private int maxPlayers;
    private ServerStatus serverStatus;
    private final String serverVersion;
    private long lastUpdate;

    public Server(String serverId, String bungee) {
        this.serverId = serverId;
        this.bungee = bungee;
        this.onlinePlayers = Bukkit.getOnlinePlayers().size();
        this.maxPlayers = Bukkit.getMaxPlayers();
        this.serverStatus = Bukkit.hasWhitelist() ? ServerStatus.WHITELISTED : ServerStatus.ONLINE;
        this.serverVersion = Bukkit.getVersion();
        this.lastUpdate = System.currentTimeMillis();
    }

    public Server(String serverId, String bungee, int onlinePlayers, int maxPlayers, String motd, ServerStatus serverStatus, String serverVersion) {
        this.serverId = serverId;
        this.bungee = bungee;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.serverStatus = serverStatus;
        this.serverVersion = serverVersion;
        this.lastUpdate = System.currentTimeMillis();
    }

    public Server(JsonObject object) {
        this.serverId = object.get("serverId").getAsString();
        this.bungee = object.get("bungee").getAsString();
        this.onlinePlayers = object.get("onlinePlayers").getAsInt();
        this.maxPlayers = object.get("maxPlayers").getAsInt();
        this.serverStatus = ServerStatus.valueOf(object.get("serverStatus").getAsString());
        this.serverVersion = object.get("serverVersion").getAsString();
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setOffline() {
        this.serverStatus = ServerStatus.OFFLINE;
        this.maxPlayers = 0;
        this.onlinePlayers = 0;
    }

    public void setServerStatus(ServerStatus status) {
        this.serverStatus = status;
    }

}
