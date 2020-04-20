package com.sagan.pluto.menu;

import com.sagan.pluto.Pluto;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cam (sagan/y0op)
 */
public abstract class Menu {

    private Map<Integer, Set<SlotFlag>> slotFlags = new HashMap<>();

    /** the default filler item used, can be overridden */
    private ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    /** A list of menu items this menu contains */
    private List<MenuItem> items;

    /** A list of integers corresponding to inventory slots that HAVE THE POTENTIAL to be filled */
    private List<Integer> rawFill = new ArrayList<>();

    /** The player viewing the menu */
    private Player viewer;
    /** The actual inventory object, kept private for safety */
    private Inventory inventory;
    /** The amount of rows this menu will have */
    private int rows;

    /**
     * Constructs a new menu object. Any sub class overriding this should properly call the super (this) constructor.
     * A new menu should be made each time a player wants to open it. This menu only exists in the scope of the list
     * of open menus above so once removed, it is scheduled for clean up by the GC
     *
     * @param rows The amount of rows this menu will have
     * @param title The title for the inventory
     */
    public Menu(int rows, String title) {
        this.rows = (rows > 6 || rows < 1) ? 6 : rows;
        this.items = new ArrayList<>();

        this.inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    /** Returns the amount of rows this menu has been defined to have */
    public int getRows() {
        return rows;
    }

    /** The player viewing/to-view this menu */
    public Player getViewer() {
        return viewer;
    }

    /**
     * Returns an optional of the menu item that would be in that slot if one exists. Empty otherwise.
     *
     * @param slot The slot to check.
     * @return Returns an optional of the menu item that would be in that slot if one exists. Empty otherwise.
     */
    public final Optional<MenuItem> getItem(int slot) {
        if (slot >= 0 && slot < (this.rows * 9)) {
            for (MenuItem item : this.items) {
                if (item.getSlot() == slot) {
                    return Optional.of(item);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Adds slot flags for a specific slot.
     * @see SlotFlag
     *
     * @param slot The slot to add or add if not present
     * @param flags The flag to add
     */
    public final void addSlotFlag(int slot, SlotFlag... flags) {

        if (slot <= (this.rows * 9) || slot >= 0) {

            if (!this.slotFlags.containsKey(slot)) {
                this.slotFlags.put(slot, new HashSet<>());
            }

            this.slotFlags.get(slot).addAll(Arrays.asList(flags));
        }
    }

    /**
     * Returns a set of slot flags that the given slot has. Returns empty if none
     *
     * @param slot the inventory slot of the slot flags
     * @return A set of slot flags that a given slot has
     */
    public final Set<SlotFlag> getSlotFlags(int slot) {
        Set<SlotFlag> slotFlags = new HashSet<>();
        if (this.slotFlags.containsKey(slot)) {
            slotFlags = this.slotFlags.get(slot);
        }
        return slotFlags;
    }

    /**
     * Removes the specified slot flags from the slot if they were present
     *
     * @param slot The slot that has these flags
     * @param flags The flag(s) to remove
     */
    public final void removeSlotFlag(int slot, SlotFlag... flags) {
        if (this.slotFlags.containsKey(slot)) {
            this.slotFlags.get(slot).removeAll(Arrays.asList(flags));

            if (this.slotFlags.get(slot).isEmpty()) {
                this.slotFlags.remove(slot);
            }
        }
    }

    /**
     * Removes all slot flags associated with a slot if any
     *
     * @param slot The slot to remove the flags from
     */
    public final void removeAllSlotFlags(int slot) {
        if (this.slotFlags.containsKey(slot)) {
            this.slotFlags.get(slot).clear();
        }
    }

    /**
     * Sets the slot flags for the specified slot. This overrides any current slot flags set. Eg. It clears all current
     * flags, if any, and applies the new one(s)
     *
     * @param slot The slot to set the flags for
     * @param flags The flag(s) to add
     */
    public final void setSlotFlags(int slot, SlotFlag... flags) {
        if (slot < (this.rows * 9) && slot >= 0) {
            this.slotFlags.put(slot, Arrays.stream(flags).collect(Collectors.toSet()));
        }
    }

    /**
     * Given all the provided MenuItems, this will set whatever item in the inventory to the items provided.
     * If there is already an item in its place, it will override it.
     *
     * @param menuItems The item(s) to set
     */
    public final void setItem(MenuItem... menuItems) {
        // The only check happening is to make sure the requested slot is not out of bounds
        Arrays.stream(menuItems).filter(menuItem -> menuItem.getSlot() < (this.rows * 9) && menuItem.getSlot() >= 0).forEach(menuItem -> {
            this.items.add(menuItem);
            this.getItem(menuItem.getSlot()).ifPresent(old -> this.items.remove(old));
            this.inventory.setItem(menuItem.getSlot(), menuItem.getItem());
        });
    }

    /**
     * Given the provided slot (if valid) and the itemstack. This will set the item in the menu at the slot to the
     * specified item. This will override any item currently in that slot.
     *
     * @param slot The slot to set
     * @param itemStack The item to set to
     */
    public final void setPlainItem(int slot, ItemStack itemStack) {
        this.setItem(new MenuItem(itemStack, slot));
    }

    /**
     * Adds a new menu item to the inventory, this can usually be called within the child class extending this one's
     * constructor. An item cannot be "added" to a slot which already contains an item
     *
     * @param menuItems The menu items to add; none, some, or one.
     */
    public final void addMenuItem(MenuItem... menuItems) {

        // Filtered out items that have their slot taken and if their slot is greater than the inventory size
        List<MenuItem> filtered = Arrays.stream(menuItems)
                .filter(menuItem ->
                        !this.items.stream().map(MenuItem::getSlot).collect(Collectors.toList()).contains(menuItem.getSlot()) &&
                        menuItem.getSlot() < (this.rows * 9) && menuItem.getSlot() >= 0)
                .collect(Collectors.toList());

        // Actually setting the item in the inventory
        filtered.forEach(menuItem -> this.inventory.setItem(menuItem.getSlot(), menuItem.getItem()));

        this.items.addAll(filtered);
    }

    /**
     * Adds a new item to the menu that is not a menu item. This item still follows rules regarding slot flags like
     * can take from and. This item has no click function. This item will only be added to the slot if there is not
     * an item in it already
     *
     * @param slot The slot to add the item to.
     * @param itemStack The item to add
     */
    public final void addPlainItem(int slot, ItemStack itemStack) {
        this.addMenuItem(new MenuItem(itemStack, slot));
    }

    /**
     * Remove an item from a menu if it exists.
     *
     * @param slot the slot to remove the item from
     */
    public final void removeItem(int slot) {
        if (slot <= (this.rows * 9) && slot >= 0) {
            this.getItem(slot).ifPresent(menuItem -> this.items.remove(menuItem));
            this.inventory.setItem(slot, null);
        }
    }

    /**
     * Will fill all the slots in the menu that are not already taken by an item except for the slot number provided
     *
     * @param except The slots to exempt from filling (slots with items are exempted by default)
     */
    public final void fillAllExcept(Integer... except) {
        this.fillAll();
        this.rawFill.removeAll(Arrays.asList(except));
    }

    /**
     * Will fill only the passed in slots so long as the passed in slots do not already contain items in them.
     *
     * @param fillSlots The slot numbers to fill with filler (slots with items are exempted by default)
     */
    public final void fillOnly(Integer... fillSlots) {
        this.rawFill = Arrays.asList(fillSlots);
    }

    /** Fills all slots in the menu that are not already taken by an item */
    public final void fillAll() {
        for (int i = 0; i < (this.rows * 9); i++) {
            rawFill.add(i);
        }
    }

    /** Redefines the item used to fill the menu (only applied to this specific menu instance) */
    protected final void setFillItem(ItemStack item) {
        filler = item;
    }

    /** Registers and opens the inventory to the player */
    public void open(Player player) {
        Pluto.currentlyOpen.add(this);
        this.viewer = player;
        redraw();
    }

    /**
     * Unregisters and closes the inventory for the player. */
    public void close() {
        if (Pluto.currentlyOpen.contains(this)) {
            this.viewer.closeInventory();
            Pluto.currentlyOpen.remove(this);
        }
    }

    public final void unRegister() {
        Pluto.currentlyOpen.remove(this);
    }

    /** Redraws the inventory, updates it. */
    public void redraw() {

        if (!Pluto.currentlyOpen.contains(this)) return;

        // Set all slots to items
        this.items.forEach(menuItem -> {
            if (menuItem.getSlot() < (this.rows * 9) && menuItem.getSlot() >= 0) {
                this.inventory.setItem(menuItem.getSlot(), menuItem.getItem());
            }
        });

        // Set all filler
        this.rawFill.forEach(integer -> {
            if (this.inventory.getItem(integer) == null) {
                this.inventory.setItem(integer, filler);
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                viewer.openInventory(inventory);
            }
        }.runTaskLater(Pluto.getInstance(), 2);
    }

    /**
     * Returns an optional of the player's currently open menu if they have one or empty if not.
     *
     * @param player The player to get the potential menu of
     * @return An optional of the player's currently open menu if they have one or empty if not.
     */
    public static Optional<Menu> getOpenMenu(Player player) {
        for (Menu menu : Pluto.currentlyOpen) {
            if (menu.getViewer().equals(player)) {
                return Optional.of(menu);
            }
        }
        return Optional.empty();
    }

    public enum SlotFlag {
        ALLOW_TAKE_FROM(),
        ALLOW_PLACE_INTO(),
        ;
    }
}