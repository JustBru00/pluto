package com.sagan.pluto.menu.testing;

import com.sagan.pluto.menu.Menu;
import com.sagan.pluto.menu.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author cam (sagan/y0op)
 */
public class TestMenu extends Menu {

    public TestMenu() {
        super(1, "My title");

        super.addMenuItem(new MenuItem(new ItemStack(Material.APPLE), 5, p -> {
            p.sendMessage("fucker");
            super.addPlainItem(6, new ItemStack(Material.SNOW));
            super.addSlotFlag(6, SlotFlag.ALLOW_TAKE_FROM);
        }));

        super.fillAllExcept(4, 5, 6, 7);
        super.fillOnly(5, 6);
    }
}