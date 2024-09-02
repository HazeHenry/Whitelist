package me.hazehenry.whitelist;

import me.hazehenry.whitelist.command.WhitelistCommand;
import me.hazehenry.whitelist.listener.WhitelistListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class WhitelistPlugin extends JavaPlugin {

    private static File whitelistfile;

    private static YamlConfiguration whitelist;

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new WhitelistListener(),this);

        getCommand("whitelist").setExecutor(new WhitelistCommand());

        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        whitelistfile = new File(getDataFolder(), "whitelist.yml");
        try {
            whitelistfile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        whitelist = new YamlConfiguration();
        try {
            whitelist.load(whitelistfile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    public static YamlConfiguration getWhitelist() {
        return whitelist;
    }

    public static File getWhitelistfile() {
        return whitelistfile;
    }

    public static WhitelistPlugin instance;

    public static WhitelistPlugin getInstance() {
        return instance;
    }

}
