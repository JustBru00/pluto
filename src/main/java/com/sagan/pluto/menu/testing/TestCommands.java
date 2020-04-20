package com.sagan.pluto.menu.testing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author cam (sagan/y0op)
 */
public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getName().equalsIgnoreCase("test") || !(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        new TestMenu().open(player);

        return false;
    }
}