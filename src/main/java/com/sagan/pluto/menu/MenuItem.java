package com.sagan.pluto.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author cam (sagan/y0op)
 */
public class MenuItem {

    /** The itemstack for this menuitem */
    private ItemStack item;
    /** The slot this item is currently in */
    private int slot;
    /** A function that will run when the item is interacted with (clicked, placed, moved etc) */
    private Consumer<Player> onClickFunction;

    /**
     * Makes a new menu item with the given parameters
     *
     * @param item The item representing this menu item
     * @param slot The slot this menu item will start in
     */
    public MenuItem(ItemStack item, int slot) {
        this(item, slot, ((player) -> {}));
    }

    /**
     * Makes a new menu item with the given parameters
     *
     * @param item The item representing this menu item
     * @param slot The slot this menu item will start in
     * @param onClickFunction The function to run when this menu item is interacted with (clicked, moved, placed etc)
     */
    public MenuItem(ItemStack item, int slot, Consumer<Player> onClickFunction) {
        this.item = item;
        this.slot = slot;
        this.onClickFunction = onClickFunction;
    }

    /**
     * @return The slot this menu item is in
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return The itemstack representing this menu item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @return The function to run when the item is interacted with
     */
    public Consumer<Player> getOnClickFunction() {
        return onClickFunction;
    }

    /**
     * Redefines the function to run when the item is interacted with.
     *
     * @param onClickFunction the new function to run when the item is interacted with.
     */
    public void setOnClickFunction(Consumer<Player> onClickFunction) {
        this.onClickFunction = onClickFunction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;

        MenuItem menuItem = (MenuItem) obj;
        return menuItem.getItem().equals(this.getItem()) && this.getSlot() == menuItem.getSlot();
    }
}