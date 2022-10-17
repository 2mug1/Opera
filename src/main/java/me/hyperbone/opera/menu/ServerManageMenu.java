package me.hyperbone.opera.menu;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.menu.button.*;
import me.hyperbone.opera.server.Server;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.medaka.Button;
import net.iamtakagi.medaka.Menu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ServerManageMenu extends Menu {

    private Server server;

    public ServerManageMenu(Plugin instance, Server server) {
        super(instance);
        this.server = server;
    }

    @Override
    public String getTitle(Player player) {
        return Style.RED + server.getServerId() + "の管理";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new ServerInfoButton(Opera.getInstance(), server));
        buttons.put(3, new ConnectToServerButton(Opera.getInstance(), server));
        buttons.put(4, new ServerRestartButton(Opera.getInstance(), server));
        buttons.put(5, new ServerStopButton(Opera.getInstance(), server));
        buttons.put(8, new BackToInfoButton(Opera.getInstance()));
        return buttons;
    }
}
