package me.hazehenry.whitelist.command;

import me.hazehenry.whitelist.WhitelistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.List;

public class WhitelistCommand implements CommandExecutor {

    private final String prefix = "§c§lW§f§lL §8» ";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(WhitelistPlugin.getInstance().getConfig().getString("permission"))) {
            sender.sendMessage("§cNincs jogod a parancs használatához.");
            return false;
        }

        if (args.length < 1) {
            sendWlHelp(sender);
            return false;
        }

        if (args.length < 2) {
            if (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("checkmessage")) {
                sendWlHelp(sender);
                return false;
            }

            boolean wl = WhitelistPlugin.getInstance().getConfig().getBoolean("whitelist");
            List<String> list = WhitelistPlugin.getWhitelist().getStringList("whitelisted");

            if (args[0].equalsIgnoreCase("on")) {
                if (wl) {
                    sender.sendMessage(prefix + "§fA fehérlista már be van kapcsolva!");
                    return false;
                }

                WhitelistPlugin.getInstance().getConfig().set("whitelist", true);
                WhitelistPlugin.getInstance().saveConfig();
                sender.sendMessage(prefix + "§aBekapcsoltad a fehérlistát!");
            }

            if (args[0].equalsIgnoreCase("off")) {
                if (!wl) {
                    sender.sendMessage(prefix + "§fA fehérlista már ki van kapcsolva!");
                    return false;
                }

                WhitelistPlugin.getInstance().getConfig().set("whitelist", false);
                WhitelistPlugin.getInstance().saveConfig();
                sender.sendMessage(prefix + "§aKikapcsoltad a fehérlistát!");
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (list.isEmpty()) {
                    sender.sendMessage("§cNincsen a fehérlistán játékos.");
                    return true;
                }

                sender.sendMessage("§8§m-------------------------");
                sender.sendMessage("§cFehérlista játékosok:");
                sender.sendMessage("");
                for (String entry : list) {
                    sender.sendMessage(" §8- §f" + entry);
                }
                sender.sendMessage("");
                sender.sendMessage("§8§m-------------------------");
            }

            if (args[0].equalsIgnoreCase("help")) {
                sendWlHelp(sender);
            }

            if (args[0].equalsIgnoreCase("reload")) {
                WhitelistPlugin.getInstance().reloadConfig();
                WhitelistPlugin.getInstance().saveConfig();
                try {
                    WhitelistPlugin.getWhitelist().load(WhitelistPlugin.getWhitelistfile());
                } catch (IOException | InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(prefix + "§aA konfig sikeresen újratöltve!");
            }

            if (args[0].equalsIgnoreCase("checkmessage")) {
                try {
                    sender.sendMessage(prefix + "§fJelenlegi üzenet: §r" + ChatColor.translateAlternateColorCodes('&', WhitelistPlugin.getInstance().getConfig().getString("whitelist-message")));
                } catch (NullPointerException ex) {
                    sender.sendMessage(prefix + "§cNincs beállított üzenet!");
                }
            }
            return true;
        }

        List<String> list = WhitelistPlugin.getWhitelist().getStringList("whitelisted");

        if (args[0].equalsIgnoreCase("add")) {
            boolean isWhitelisted = false;
            for (String entry : list) {
                if (entry.equalsIgnoreCase(args[1])) {
                    isWhitelisted = true;
                    break;
                }
            }
            if (isWhitelisted) {
                sender.sendMessage(prefix + "§cEz a játékos már whitelistelve van!");
                return false;
            }

            String name = Bukkit.getOfflinePlayer(args[1]).getName();
            list.add(name);
            WhitelistPlugin.getWhitelist().set("whitelisted", list);
            sender.sendMessage(prefix + "§aHozzáadtad §f" + name + " §ajátékost a fehérlistához!");
            try {WhitelistPlugin.getWhitelist().save(WhitelistPlugin.getWhitelistfile());
            } catch (IOException e) {throw new RuntimeException(e);}
        }

        if (args[0].equalsIgnoreCase("remove")) {
            boolean isWhitelisted = false;
            String name = "";
            for (String entry : list) {
                if (entry.equalsIgnoreCase(args[1])) {
                    isWhitelisted = true;
                    name = entry;
                    break;
                }
            }

            if (!isWhitelisted) {
                sender.sendMessage(prefix + "§cEz a játékos nincs whitelistelve!");
                return false;
            }

            list.remove(name);
            String player = Bukkit.getOfflinePlayer(name).getName();
            WhitelistPlugin.getWhitelist().set("whitelisted", list);
            sender.sendMessage(prefix + "§aLevetted §f" + player + " §ajátékost a fehérlistáról!");
            try { WhitelistPlugin.getWhitelist().save(WhitelistPlugin.getWhitelistfile());
            } catch (IOException e) { throw new RuntimeException(e); }
        }

        if (args[0].equalsIgnoreCase("setmessage")) {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
            WhitelistPlugin.getInstance().getConfig().set("whitelist-message", builder.toString());
            sender.sendMessage(prefix + "§fSikeresen §afrissítetted §fa whitelist üzenetet.");
        }
        return true;
    }

    public void sendWlHelp(CommandSender p) {
        p.sendMessage("§8§m--------------------------------------------------");
        if (WhitelistPlugin.getInstance().getConfig().getBoolean("whitelist")) {
            p.sendMessage("§c§lWHITELIST §f§lPLUGIN §8- §aAktív");
        } else {
            p.sendMessage("§c§lWHITELIST §f§lPLUGIN §8- §cInaktív");
        }
        p.sendMessage(" §7Parancsok:");
        p.sendMessage("  §c/wl <on/off> §8- §fFehérlista ki/be kapcsolása");
        p.sendMessage("  §c/wl <add/remove> §8- §fJátékosok hozzáadása / eltávolítása.");
        p.sendMessage("  §c/wl list §8- §fFehérlistán lévő játékosok kilistázása.");
        p.sendMessage("  §c/wl setmessage <üzenet> §8- §fWhitelist üzenet beállítása.");
        p.sendMessage("  §c/wl checkmessage §8- §fWhitelist üzenet megnézése.");
        p.sendMessage("  §c/wl reload §8- §fKonfig újratöltése.");
        p.sendMessage("  §c/wl help §8- §fEnnek az üzenetnek a megjelenítése.");
        p.sendMessage("§8§m--------------------------------------------------");
    }
}
