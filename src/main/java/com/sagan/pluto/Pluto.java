package com.sagan.pluto;

import com.sagan.pluto.menu.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pluto extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
