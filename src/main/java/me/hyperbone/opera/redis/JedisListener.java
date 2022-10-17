package me.hyperbone.opera.redis;

import com.google.gson.JsonObject;
import me.hyperbone.hakobi.hakobi.letter.Letter;
import me.hyperbone.hakobi.hakobi.letter.handler.IncomingLetterHandler;
import me.hyperbone.hakobi.hakobi.letter.listener.LetterListener;
import me.hyperbone.opera.Opera;
import me.hyperbone.opera.OperaConfig;
import me.hyperbone.opera.server.Server;
import net.iamtakagi.iroha.Style;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class JedisListener implements LetterListener {

    @IncomingLetterHandler("update")
    public void receiveUpdate(JsonObject data) {
        Opera.getInstance().getServerManager().initOrUpdateServer(
                new Server(data.get("serverId").getAsString(), data.get("bungee").getAsString()));
    }

    @IncomingLetterHandler("execute")
    public void receiveExecute(JsonObject data) {
        if (data.get("server").getAsString().equalsIgnoreCase(OperaConfig.loadYamlConfig().getString("server.id")) ||
                data.get("server").getAsString().equalsIgnoreCase("all")) {

            //遅延させないとエラー吐く
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.dispatchCommand(Opera.getInstance().getServer().getConsoleSender(), data.get("command").getAsString());
                }
            }.runTaskLater(Opera.getInstance(), 5L);

            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("opera.log"))
                    .forEach(player -> player.sendMessage(Opera.getInstance().getPrefix() + Style.GREEN + "コマンドを実行しました。",
                            Opera.getInstance().getPrefix() + Style.AQUA + "サーバー: " + Style.WHITE + data.get("requester").getAsString(),
                            Opera.getInstance().getPrefix() + Style.AQUA + "コマンド: " + Style.WHITE + data.get("command").getAsString(),
                            Opera.getInstance().getPrefix() + Style.GRAY + "(" + data.get("sender").getAsString() + "からのリクエスト)"));

            Bukkit.getConsoleSender().sendMessage(Opera.getInstance().getPrefix() + Style.GREEN + "コマンドを実行しました。",
                    Opera.getInstance().getPrefix() + Style.AQUA + "サーバー: " + Style.WHITE + data.get("requester").getAsString(),
                    Opera.getInstance().getPrefix() + Style.AQUA + "コマンド: " + Style.WHITE + data.get("command").getAsString(),
                    Opera.getInstance().getPrefix() + Style.GRAY + "(" + data.get("sender").getAsString() + "からのリクエスト)");
        }
    }

    @IncomingLetterHandler("requestRestart")
    public void receiveRequestRestart(JsonObject data) {
        if (data.get("server").getAsString().equalsIgnoreCase(OperaConfig.loadYamlConfig().getString("server.id"))) {
            Bukkit.getConsoleSender().sendMessage(Opera.getInstance().getPrefix() + Style.RED + "サーバー再起動のリクエストを受信しました。5秒後に再起動します。");
            Bukkit.getScheduler().runTaskLater(Opera.getInstance(), () ->
                    Opera.getInstance().getServer().spigot().restart(), 5 * 20);

            Map<String, String> accepted = new HashMap<>();
            accepted.put("sender", OperaConfig.loadYamlConfig().getString("server.id"));
            accepted.put("type", "restart");
            Letter letter = new Letter("requestAccepted", accepted);
            Opera.getInstance().getJedisManager().getHakobi().sendLetter(letter);
        }
    }

    @IncomingLetterHandler("requestStop")
    public void receiveRequestStop(JsonObject data) {
        if (data.get("server").getAsString().equalsIgnoreCase(OperaConfig.loadYamlConfig().getString("server.id"))) {
            Bukkit.getConsoleSender().sendMessage(Opera.getInstance().getPrefix() + Style.RED + "サーバー停止のリクエストを受信しました。5秒後に停止します。");
            Bukkit.getScheduler().runTaskLater(Opera.getInstance(), () ->
                    Opera.getInstance().getServer().shutdown(), 5 * 20);

            Map<String, String> accepted = new HashMap<>();
            accepted.put("sender", OperaConfig.loadYamlConfig().getString("server.id"));
            accepted.put("type", "stop");
            Letter letter = new Letter("requestAccepted", accepted);
            Opera.getInstance().getJedisManager().getHakobi().sendLetter(letter);
        }
    }

    @IncomingLetterHandler("requestAccepted")
    public void receiveRequestAccepted(JsonObject data) {
        switch (data.get("type").getAsString()) {
            case "restart" ->
                    Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("opera.log")).forEach(
                            player -> player.sendMessage(Opera.getInstance().getPrefix() +
                                    Style.WHITE + Style.BOLD + data.get("sender").getAsString() + Style.RESET + Style.RED + "が５秒後再起動します。")
                    );
            case "stop" ->
                    Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("opera.log")).forEach(
                            player -> player.sendMessage(Opera.getInstance().getPrefix() +
                                    Style.WHITE + Style.BOLD + data.get("sender").getAsString() + Style.RESET + Style.RED + "が５秒後停止します。")
                    );
        }
    }
}
