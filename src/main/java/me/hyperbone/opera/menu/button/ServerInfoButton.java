package me.hyperbone.opera.menu.button;

import me.hyperbone.opera.server.Server;
import net.iamtakagi.iroha.ItemBuilder;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.medaka.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ServerInfoButton extends Button {

    private Server server;

    public ServerInfoButton(Plugin instance, Server server) {
        super(instance);
        this.server = server;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.BOOK)
                .name(Style.GOLD + Style.BOLD + server.getServerId())
                .lore(Arrays.asList(
                        Style.YELLOW + "状態: " + Style.WHITE + server.getServerStatus().getFormat(),
                        Style.YELLOW + "接続人数: " + Style.WHITE + server.getOnlinePlayers() + "/" + server.getMaxPlayers(),
                        Style.YELLOW + "Spigotバージョン: " + Style.WHITE + server.getServerVersion()
                ))
                .build();
    }
}
