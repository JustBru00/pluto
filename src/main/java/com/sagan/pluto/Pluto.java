package com.sagan.pluto;

import com.sagan.pluto.menu.Menu;
import com.sagan.pluto.menu.MenuListener;
import com.sagan.pluto.menu.testing.TestCommands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Pluto extends JavaPlugin {

    /** a list of all the currently open menus */
    public static List<Menu> currentlyOpen = new ArrayList<>();

    private static Pluto instance;
    public static Pluto getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getCommand("test").setExecutor(new TestCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
