package com.sagan.pluto.menu.event;

import com.sagan.pluto.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * @author cam (sagan/y0op)
 */
public class PlayerChangeMenuContentsEvent extends MenuEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /** The specific item that was added or removed */
    private ItemStack item;
    /** The slot the item was added to or removed from */
    private int slot;
    /** The type of change that happened */
    private ChangeType changeType;

    public PlayerChangeMenuContentsEvent(Player who, Menu menu, ItemStack item, int slot, ChangeType type) {
        super(who, menu);

        this.item = item;
        this.slot = slot;
        this.changeType = type;
    }

    /**
     * @return The type of change that happened
     */
    public ChangeType getChangeType() {
        return changeType;
    }

    /**
     * @return The slot the item was added to or removed from
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return The specific item that was added or removed
     */
    public ItemStack getItem() {
        return item;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum ChangeType {
        ADD_TO(),
        TAKE_FROM(),
        ;
    }
}