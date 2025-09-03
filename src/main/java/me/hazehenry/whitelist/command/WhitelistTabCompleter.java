package me.hazehenry.whitelist.command;

import me.hazehenry.whitelist.WhitelistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WhitelistTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return List.of("on", "off", "list", "add", "remove", "setmessage", "checkmessage", "reload");
        }

        if (args.length <= 3) {
            if (args[0].equalsIgnoreCase("setmessage")) {
                return List.of("<üzenet>");
            }

            if (args[0].equalsIgnoreCase("remove")) {
                List<String> finalList = new ArrayList<>();
                List<String> list = WhitelistPlugin.getWhitelist().getStringList("whitelisted");
                for (String entry : list) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry);
                    finalList.add(player.getName());
                }
                return finalList;
            }
        }

        List<String> finalList = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            finalList.add(player.getName());
        }

        return finalList;
    }
}
