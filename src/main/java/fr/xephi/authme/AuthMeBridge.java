package fr.xephi.authme;

import fr.xephi.authme.database.DataSource;
import fr.xephi.authme.database.DatabaseCalls;
import fr.xephi.authme.database.MySQL;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Xephi59
 *
 */
public class AuthMeBridge extends Plugin {

    private static AuthMeBridge instance;
    private DataSource database;

    @Override
    public void onEnable() {
        instance = this;
        database = new MySQL();
        database = new DatabaseCalls(database);
        this.getProxy().getPluginManager().registerListener(this, new AuthMeListener(database));
    }

    public static AuthMeBridge getInstance() {
        return instance;
    }
}
