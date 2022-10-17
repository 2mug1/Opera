package me.hyperbone.opera.menu.button;

import me.hyperbone.hakobi.hakobi.letter.Letter;
import me.hyperbone.opera.Opera;
import me.hyperbone.opera.menu.ServerManageMenu;
import me.hyperbone.opera.server.Server;
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

import java.util.HashMap;
import java.util.Map;

public class ServerStopButton extends Button {

    private Server server;

    public ServerStopButton(Plugin instance, Server server) {
        super(instance);
        this.server = server;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.TNT)
                .name(Style.RED + Style.BOLD + server.getServerId() + "を停止する")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new ConfirmMenu(Opera.getInstance(), Style.YELLOW + server.getServerId() + "を停止しますか？", (TypeCallback<Boolean>) data -> {
            if (data) {
                player.sendMessage(Style.YELLOW + server.getServerId() + "に停止のリクエストを送信しました。");
                Map<String, String> request = new HashMap<>();
                request.put("server", server.getServerId());
                Letter letter = new Letter("requestStop", request);
                Opera.getInstance().getJedisManager().getHakobi().sendLetter(letter);
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
