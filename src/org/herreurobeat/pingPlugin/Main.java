package org.herreurobeat.pingPlugin;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main extends JavaPlugin {

    String supportedVersion = "1.16";
    String pluginVersion = "1.0";

    int serverVersion;

    @Override
    public void onEnable() {
        System.out.println("pingPlugin for version " + supportedVersion + " enabled!");

        //Get server version once on startup to reduce load when using command
        String serverVersionStr   = getServer().getBukkitVersion();
        String serverVersionMajor = serverVersionStr.split("\\.")[0] + serverVersionStr.split("\\.")[1].split("-")[0]; //get major mc version

        serverVersion = Integer.parseInt(serverVersionMajor.replaceAll("\\.", "")); //convert to number to make comparable
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


        try {

            Player player = (Player) sender;

            //Check which version the server uses. 1.17 and above have a getPing() method, otherwise we need to access the CraftPlayer Class which has a different path each version
            if (serverVersion >= 117) {

                int ping = player.getPing();
                sender.sendMessage("§aYour ping: §e" + ping + "ms");

            } else {

                //Get CraftPlayer class dynamically based on which version the server is running on
                Class<?> CraftPlayerClass = Class.forName(getServer().getClass().getPackage().getName() + ".entity.CraftPlayer"); //get class
                Object CraftPlayer        = CraftPlayerClass.cast(player); //make new object

                Method getHandle = CraftPlayer.getClass().getMethod("getHandle"); //get getHandle method from CraftPlayer

                Object EntityPlayer = getHandle.invoke(CraftPlayer); //make new EntityPlayer with getHandle() method
                Field  ping         = EntityPlayer.getClass().getDeclaredField("ping"); //get field ping from EntityPlayer

                sender.sendMessage("§aYour ping: §e" + ping.getInt(EntityPlayer) + "ms");

            }

        } catch (Exception err) {
            sender.sendMessage("Error: " + err.getMessage());
        }
    }
}