package fr.xephi.authme.AuthMeBungeeBridge;

public final class Settings {

    private AuthMeBridge plugin;

    protected YamlConfig configFile = null;
    public static String getMySQLHost;
    public static String getMySQLPort;
    public static String getMySQLUsername;
    public static String getMySQLPassword;
    public static String getMySQLDatabase;
    public static String getMySQLTablename;
    public static String getMySQLColumnName;
    public static String getMySQLColumnLogged;

    public Settings(AuthMeBridge plugin) {
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

        getMySQLHost = configFile.getConfig().getString("DataSource.mySQLHost", "127.0.0.1");
        getMySQLPort = configFile.getConfig().getString("DataSource.mySQLPort", "3306");
        getMySQLUsername = configFile.getConfig().getString("DataSource.mySQLUsername", "authme");
        getMySQLPassword = configFile.getConfig().getString("DataSource.mySQLPassword", "12345");
        getMySQLDatabase = configFile.getConfig().getString("DataSource.mySQLDatabase", "authme");
        getMySQLTablename = configFile.getConfig().getString("DataSource.mySQLTablename", "authme");
        getMySQLColumnName = configFile.getConfig().getString("DataSource.mySQLColumnName", "username");
        getMySQLColumnLogged = configFile.getConfig().getString("DataSource.mySQLColumnLogged", "isLogged");

    }
}
