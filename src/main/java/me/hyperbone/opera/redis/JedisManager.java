package me.hyperbone.opera.redis;

import com.google.gson.Gson;
import lombok.Getter;
import me.hyperbone.hakobi.hakobi.Hakobi;
import me.hyperbone.opera.OperaConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class JedisManager {

    private JedisPool jedisPool;
    private Jedis jedis;
    private Gson gson;
    private Hakobi hakobi;
    private String jedisPassword;
    private boolean jedisAuth;


    public JedisManager(Plugin plugin) {
        gson = new Gson();

        final String databasePath = "database.redis.";
        YamlConfiguration yaml = OperaConfig.loadYamlConfig();
        this.jedisAuth = yaml.getBoolean(databasePath + "auth.enabled");
        this.jedisPassword = yaml.getString(databasePath + "auth.password") != null ?
                yaml.getString(databasePath + "auth.password") : null;

        this.jedisPool = new JedisPool(
                yaml.getString(databasePath + "address"),
                yaml.getInt(databasePath + "port"));
        this.jedis = this.jedisPool.getResource();
        this.authenticate();

        this.hakobi = new Hakobi(
                yaml.getString(databasePath + "channel"),
                jedisPool,
                jedisPassword,
                gson);
        hakobi.registerListener(new JedisListener());
    }

    public void authenticate() {
        if (this.jedisAuth) {
            this.jedis.auth(this.jedisPassword);
        }
    }
}
