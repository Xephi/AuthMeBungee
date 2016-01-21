package fr.xephi.authmebungee;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Settings {

    private AuthMeBungee plugin;

    protected YamlConfig configFile = null;
    public static List<String> revokePermissions = new ArrayList<String>();
    public static boolean isAuthMeSessionEnabled = false;

    public Settings(AuthMeBungee plugin) {
        this.plugin = plugin;
        try {
            this.configFile = new YamlConfig("config.yml", plugin);
            this.configFile.saveDefaultConfig();
            this.configFile.loadConfig();
            this.loadConfigOptions();
        } catch (Exception ex) {
            plugin.getLogger()
                    .log(Level.INFO, "Cannot load plugin configuration, it can have an unexpected side effect...", ex);
        }
    }

    public void loadConfigOptions() {
        if (configFile == null)
            return;
        plugin.getLogger().info("Loading Configuration File...");

        revokePermissions = configFile.getConfig().getStringList("permissionsWhitelist");
        isAuthMeSessionEnabled = configFile.getConfig().getBoolean("isAuthMeSessionEnabled");
    }
}
