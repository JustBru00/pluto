package com.sagan.pluto.menu.event;

import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author cam (sagan/y0op)
 */
public abstract class MenuEvent extends PlayerEvent {

    /** The menu that was interacted with */
    private Menu menu;

    public MenuEvent(Player who, Menu menu) {
        super(who);

        this.menu = menu;
    }

    /**
     * @return The menu in which the event was fired
     */
    public Menu getMenu() {
        return menu;
    }
}