package me.hyperbone.opera.command;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.OperaAPI;
import me.hyperbone.opera.OperaConfig;
import me.hyperbone.opera.menu.ServerInfoMenu;
import me.hyperbone.opera.server.Server;
import me.hyperbone.hakobi.hakobi.letter.Letter;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.kodaka.annotation.CPL;
import net.iamtakagi.kodaka.annotation.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@CommandMeta(label = {"opera", "servermonitor"}, permission = "opera.use", subcommands = true)
public class OperaCommand {

    public void execute(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            new ServerInfoMenu(Opera.getInstance()).openMenu(player);
        } else {
            sender.sendMessage(Opera.getInstance().getPrefix() + Style.WHITE + "/opera list",
                    Opera.getInstance().getPrefix() + Style.WHITE + "/opera info <サーバー名/all>",
                    Opera.getInstance().getPrefix() + Style.WHITE + "/opera execute <サーバー名/all> <コマンド>");
        }
    }

    @CommandMeta(label = {"help"})
    public class HelpCommand extends OperaCommand {

        public void execute(CommandSender sender) {
            sender.sendMessage(Opera.getInstance().getPrefix() + Style.WHITE + "/opera list",
                    Opera.getInstance().getPrefix() + Style.WHITE + "/opera info <サーバー名/all>",
                    Opera.getInstance().getPrefix() + Style.WHITE + "/opera execute <サーバー名/all> <コマンド>");
        }
    }

    @CommandMeta(label = {"execute", "executecommand", "executecmd", "command", "runcmd", "runcommand"})
    public class RequestToExecuteCommand extends OperaCommand {

        public void execute(CommandSender sender, @CPL("server") String server, String command) {
            if (OperaAPI.getInstance().getServerByName(server) == null && !server.equalsIgnoreCase("all")) {
                sender.sendMessage(Style.RED + "サーバーが見つかりません。");
                return;
            }

            Map<String, String> data = new HashMap<>();
            String senderName = sender instanceof Player ? ((Player) sender).getName() : "コンソール";
            data.put("requester", OperaConfig.loadYamlConfig().getString("server.id"));
            data.put("server", server);
            data.put("command", command);
            data.put("sender", senderName);
            Letter letter = new Letter("execute", data);
            Opera.getInstance().getJedisManager().getHakobi().sendLetter(letter);
            sender.sendMessage(server.equalsIgnoreCase("all") ? Style.GREEN + "接続されているすべてのサーバーにコマンドリクエストを送信しました。" :
                    Style.GREEN + server + "にコマンドリクエストを送信しました。");
        }
    }

    @CommandMeta(label = {"list"})
    public class ListCommand extends OperaCommand {

        public void execute(CommandSender sender) {
            Opera.getInstance().getServerManager().getServerMap().values().forEach(server -> {
                sender.sendMessage(Style.CHAT_BAR,
                        Style.AQUA + "サーバー名: " + Style.WHITE + server.getServerId(),
                        Style.AQUA + "接続人数: " + Style.WHITE + server.getOnlinePlayers() + "/" + server.getMaxPlayers(),
                        Style.AQUA + "状態: " + Style.WHITE + server.getServerStatus().getFormat(),
                        Style.AQUA + "Spigotバージョン: " + Style.WHITE + server.getServerVersion(),
                        Style.CHAT_BAR);
            });
        }
    }

    @CommandMeta(label = {"get", "info", "information"})
    public class InfoCommand extends OperaCommand {

        public void execute(CommandSender sender, @CPL("server") String server) {
            if (OperaAPI.getInstance().getServerByName(server) != null) {
                Server target = OperaAPI.getInstance().getServerByName(server);
                if (target == null) {
                    sender.sendMessage(Style.RED + "サーバーが見つかりません。");
                    return;
                }

                sender.sendMessage(Style.CHAT_BAR,
                        Style.AQUA + "サーバー名: " + Style.WHITE + target.getServerId(),
                        Style.AQUA + "接続人数: " + Style.WHITE + target.getOnlinePlayers() + "/" + target.getMaxPlayers(),
                        Style.AQUA + "状態: " + Style.WHITE + target.getServerStatus().getFormat(),
                        Style.AQUA + "Spigotバージョン: " + Style.WHITE + target.getServerVersion(),
                        Style.CHAT_BAR);
            } else {
                if (server.equalsIgnoreCase("all")) {
                    Opera.getInstance().getServerManager().getServerMap().values().forEach(target -> {
                        sender.sendMessage(Style.CHAT_BAR,
                                Style.AQUA + "サーバー名: " + Style.WHITE + target.getServerId(),
                                Style.AQUA + "接続人数: " + Style.WHITE + target.getOnlinePlayers() + "/" + target.getMaxPlayers(),
                                Style.AQUA + "状態: " + Style.WHITE + target.getServerStatus().getFormat(),
                                Style.AQUA + "Spigotバージョン: " + Style.WHITE + target.getServerVersion(),
                                Style.CHAT_BAR);
                    });
                }
                sender.sendMessage(Style.RED + "サーバーが見つかりません。");
            }
        }
    }

    @CommandMeta(label = {"debug"})
    public class DebugCommand extends OperaCommand {

        public void execute(CommandSender sender) {
            sender.sendMessage(Style.PINK + "オンライン: " + Style.WHITE + OperaAPI.getInstance().getOnlinePlayers());
        }
    }
}
