package me.hyperbone.opera.menu;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.server.Server;
import net.iamtakagi.iroha.ItemBuilder;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.medaka.Button;
import net.iamtakagi.medaka.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ServerInfoMenu extends PaginatedMenu {

    public ServerInfoMenu(Plugin instance) {
        super(instance);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Style.RED + "サーバー管理";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Opera.getInstance().getServerManager().getServerMap().values().forEach(server -> {
            buttons.put(buttons.size(), new ServerInfoButton(instance, server));
        });

        return buttons;
    }

    private class ServerInfoButton extends Button {

        private Server server;

        public ServerInfoButton(org.bukkit.plugin.Plugin instance, Server server) {
            super(instance);
            this.server = server;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.ANVIL)
                    .name(Style.GOLD + Style.BOLD + server.getServerId())
                    .lore(Arrays.asList(
                            Style.YELLOW + "状態: " + Style.WHITE + server.getServerStatus().getFormat(),
                            Style.YELLOW + "接続人数: " + Style.WHITE + server.getOnlinePlayers() + "/" + server.getMaxPlayers(),
                            Style.YELLOW + "Spigotバージョン: " + Style.WHITE + server.getServerVersion()
                    ))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new ServerManageMenu(Opera.getInstance(), server).openMenu(player);
        }
    }
}
