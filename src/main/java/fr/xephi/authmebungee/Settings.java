package fr.xephi.authmebungee;

import java.util.ArrayList;
import java.util.List;

public final class Settings {

    private AuthMeBungee plugin;

    protected YamlConfig configFile = null;
    public static List<String> revokePermissions = new ArrayList<String>();

    public Settings(AuthMeBungee plugin) {
        this.plugin = plugin;
        try {
            this.configFile = new YamlConfig("config.yml", plugin);
            this.configFile.loadConfig();
            this.loadConfigOptions();
        } catch (Exception e) {
            plugin.getLogger().info("Cannot load configuration...");
        }
    }

    public void loadConfigOptions() {
        if (configFile == null)
            return;
        plugin.getLogger().info("Loading Configuration File...");

        revokePermissions = configFile.getConfig().getStringList("permissionsNeedsAuth");
    }
}
