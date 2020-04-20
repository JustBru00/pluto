package com.sagan.pluto.menu;

import com.sagan.pluto.Pluto;
import com.sagan.pluto.menu.event.PlayerChangeMenuContentsEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Optional;

/**
 * @author cam (sagan/y0op)
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu.getOpenMenu((Player) event.getPlayer()).ifPresent(Menu::unRegister);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Pluto.currentlyOpen.forEach(menu -> {
            if (menu.getViewer().equals(event.getPlayer())) {
                event.setCancelled(true);
            }
        });
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Optional<Menu> menuOptional = Menu.getOpenMenu(player);

        if (!menuOptional.isPresent()) {
            return;
        }

        Menu menu = menuOptional.get();

        // If they're clicking in their own inventory, ignore it
        if (event.getSlot() >= (menu.getRows() * 9)) {
            return;
        }

        switch (event.getAction()) {
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
            case DROP_ALL_CURSOR:
            case DROP_ONE_CURSOR:
            case MOVE_TO_OTHER_INVENTORY:
            case SWAP_WITH_CURSOR:
            case COLLECT_TO_CURSOR:
            case HOTBAR_MOVE_AND_READD:
            case CLONE_STACK:
            case HOTBAR_SWAP:
                event.setCancelled(true);
                break;
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                if (!menu.getSlotFlags(event.getSlot()).contains(Menu.SlotFlag.ALLOW_PLACE_INTO)) {
                    event.setCancelled(true);
                } else {
                    Bukkit.getPluginManager().callEvent(new PlayerChangeMenuContentsEvent(player,
                            menu, event.getCursor(), event.getSlot(), PlayerChangeMenuContentsEvent.ChangeType.ADD_TO));
                }
                break;
            case PICKUP_ALL:
            case PICKUP_ONE:
            case PICKUP_HALF:
            case PICKUP_SOME:
                if (!menu.getSlotFlags(event.getSlot()).contains(Menu.SlotFlag.ALLOW_TAKE_FROM)) {
                    event.setCancelled(true);
                } else {
                    Bukkit.getPluginManager().callEvent(new PlayerChangeMenuContentsEvent(player,
                            menu, event.getCursor(), event.getSlot(), PlayerChangeMenuContentsEvent.ChangeType.TAKE_FROM));
                }

                // Calling the function associated with this item.
                final Optional<MenuItem> item = menu.getItem(event.getSlot());
                item.ifPresent(menuItem -> menuItem.getOnClickFunction().accept(player));

                break;
        }
    }
}