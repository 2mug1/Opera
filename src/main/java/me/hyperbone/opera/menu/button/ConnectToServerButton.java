package me.hyperbone.opera.menu.button;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.menu.ServerManageMenu;
import me.hyperbone.opera.server.Server;
import net.iamtakagi.iroha.BungeeUtil;
import net.iamtakagi.iroha.ItemBuilder;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.iroha.callback.TypeCallback;
import net.iamtakagi.medaka.Button;
import net.iamtakagi.medaka.menus.ConfirmMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ConnectToServerButton extends Button {

    private Server server;

    public ConnectToServerButton(Plugin instance, Server server) {
        super(instance);
        this.server = server;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ENDER_PEARL)
                .name(Style.AQUA + Style.BOLD + server.getServerId() + "に移動する")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new ConfirmMenu(Opera.getInstance(), Style.YELLOW + server.getServerId() + "に移動しますか？", (TypeCallback<Boolean>) data -> {
            if (data) {
                BungeeUtil.connect(Opera.getInstance(), player, server.getBungee());
            } else {
                new ServerManageMenu(Opera.getInstance(), server).openMenu(player);
            }
        }, true) {
            @Override
            public void onClose(Player player) {
                if (!isClosedByMenu()) {
                    new ServerManageMenu(Opera.getInstance(), server).openMenu(player);
                }
            }
        }.openMenu(player);
    }
}
