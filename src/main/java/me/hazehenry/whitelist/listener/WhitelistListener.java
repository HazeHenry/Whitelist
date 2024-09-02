package me.hazehenry.whitelist.listener;

import me.hazehenry.whitelist.WhitelistPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;
import java.util.Objects;

public class WhitelistListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleWhitelist(AsyncPlayerPreLoginEvent e) {
        String p = e.getName();
        boolean wl = WhitelistPlugin.getInstance().getConfig().getBoolean("whitelist");
        List<String> list = Objects.requireNonNull(WhitelistPlugin.getWhitelist().getStringList("whitelisted"));
        if (wl) {
            boolean canJoin = false;
            for (String entry : list) {
                if (entry.equalsIgnoreCase(p)) {
                    canJoin = true;
                    break;
                }
            }
            if (!canJoin) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);
                e.setKickMessage(ChatColor.translateAlternateColorCodes('&',WhitelistPlugin.getInstance().getConfig().getString("whitelist-message")));
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.isOp()) {
                        pl.sendMessage("§c§lW§f§lL §8» §e" + p + " §cMegpróbált felcsatlakozni, azonban a fehérlista bevan kapcsolva!");
                    }
                }
            }
        }
    }
}
