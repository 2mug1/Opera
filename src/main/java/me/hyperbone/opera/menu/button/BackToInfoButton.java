package me.hyperbone.opera.menu.button;

import me.hyperbone.opera.Opera;
import me.hyperbone.opera.menu.ServerInfoMenu;
import net.iamtakagi.iroha.ItemBuilder;
import net.iamtakagi.iroha.Style;
import net.iamtakagi.medaka.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BackToInfoButton extends Button {

    public BackToInfoButton(Plugin instance) {
        super(instance);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name(Style.GRAY + "サーバーリストに戻る")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new ServerInfoMenu(Opera.getInstance()).openMenu(player);
    }
}
