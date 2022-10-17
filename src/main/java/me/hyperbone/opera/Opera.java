package me.hyperbone.opera;

import me.hyperbone.opera.command.OperaCommand;
import me.hyperbone.opera.redis.JedisManager;
import me.hyperbone.opera.server.ServerManager;
import me.hyperbone.opera.task.OnlinePlayerCountTask;
import me.hyperbone.opera.task.ServerHeartbeatTask;
import me.hyperbone.opera.task.ServerUpdateTask;
import lombok.Getter;
import me.hyperbone.faygo.Dependency;
import me.hyperbone.faygo.Faygo;
import me.hyperbone.faygo.Repository;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.kodaka.Kodaka;
import net.iamtakagi.medaka.Medaka;
import org.bukkit.plugin.java.JavaPlugin;

public class Opera extends JavaPlugin {

    @Getter
    private static Opera instance;
    @Getter
    private JedisManager jedisManager;
    @Getter
    private ServerManager serverManager;
    @Getter
    private String prefix = Style.GOLD + "[" + Style.YELLOW + "Opera" + Style.GOLD + "] ";
    private static boolean setup;

    @Override
    public void onEnable() {
        setup = true;
        instance = this;
        this.saveDefaultConfig();

        new Faygo(this).getDependencyBuilder()
                .add(new Dependency(
                        Repository.MAVENCENTRAL,
                        "redis.clients",
                        "jedis",
                        "4.2.3"))
                .add(new Dependency(
                        Repository.MAVENCENTRAL,
                        "org.apache.commons",
                        "commons-pool2",
                        "2.11.1"
                ))
                .add(new Dependency(
                        Repository.MAVENCENTRAL,
                        "com.google.code.gson",
                        "gson",
                        "2.9.0"
                ))
                .add(new Dependency(
                        Repository.MAVENCENTRAL,
                        "org.json",
                        "json",
                        "20220924"
                ))
                .load();

        serverManager = new ServerManager(this);
        jedisManager = new JedisManager(this);

        new ServerUpdateTask(this, serverManager);
        new ServerHeartbeatTask(this, serverManager);
        new OnlinePlayerCountTask(this, serverManager);

        new OperaAPI(this, serverManager);
        new Kodaka(this).registerCommand(new OperaCommand());
        Medaka.init(this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        setup = false;
    }

    @Override
    public void onDisable() {
    }

    public static boolean isSetup() {
        return setup;
    }
}
