package org.herreurobeat.pingPlugin;

import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

    String supportedVersion = "1.16";
    String pluginVersion = "1.0";

    @Override
    public void onEnable() {
        System.out.println("pingPlugin for version " + supportedVersion + " enabled!");
    }

    //Handle command execution
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { //handle calling command methods

        //Since this is a small plugin I will handle every command in this file
        switch (cmd.getName()) { //handle execution of each command
            case "pinginfo":
                pinginfoCommand(sender, args);
                break;
            case "ping":
                pingCommand(sender, args);
                break;
            default:
                break;
        }

        return false;
    }


    /**
     * Handles the pinginfo command
     * @param sender The player who used the command
     * @param args The arguments the user provided
     */
    public void pinginfoCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) { //check if sender is a player
            Player p = (Player) sender; //convert to player
            if (!p.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK))
                return; //check gamerule and stop if send command feedback is false
        }
        sender.sendMessage("pingPlugin by 3urobeat v" + pluginVersion + " \n----\nType /ping to see your ping in ms to the server.\n----\nhttps://github.com/HerrEurobeat/pingPlugin");
    }


    /**
     * Handles the ping command itself
     * @param sender The player who used the command
     * @param args The arguments the user provided
     */
    public void pingCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.getWorld().getGameRuleValue(GameRule.SEND_COMMAND_FEEDBACK))
                return; //check gamerule and stop if send command feedback is false
        }

        EntityPlayer player = ((CraftPlayer) sender).getHandle();
        sender.sendMessage("§aYour ping: §e" + player.ping + "ms");
    }
}