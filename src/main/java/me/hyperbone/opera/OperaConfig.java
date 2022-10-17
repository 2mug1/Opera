package me.hyperbone.opera;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class OperaConfig {

    public static String CONFIG_YAML_DEST = Opera.getInstance().getDataFolder() + File.separator + "config.yml";

    public static YamlConfiguration loadYamlConfig() {
        return YamlConfiguration.loadConfiguration(new File(CONFIG_YAML_DEST));
    }
}
