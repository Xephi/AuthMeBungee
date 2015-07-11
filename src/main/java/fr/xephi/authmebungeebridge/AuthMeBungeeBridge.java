package fr.xephi.authmebungeebridge;

import fr.xephi.authmebungeebridge.database.DataSource;
import fr.xephi.authmebungeebridge.database.DatabaseCalls;
import fr.xephi.authmebungeebridge.database.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Xephi59
 *
 */
public class AuthMeBungeeBridge extends Plugin {

    private static AuthMeBungeeBridge instance;
    private DataSource database;

    @Override
    public void onEnable() {
        instance = this;
        database = new MySQL();
        database = new DatabaseCalls(database);
        this.getProxy().getPluginManager().registerListener(this, new AuthMeListener(database));
    }

    public static AuthMeBungeeBridge getInstance() {
        return instance;
    }
}
