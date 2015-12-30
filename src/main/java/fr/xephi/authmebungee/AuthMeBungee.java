package fr.xephi.authmebungee;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Xephi59
 *
 */
public class AuthMeBungee extends Plugin {

    private static AuthMeBungee instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getProxy().getPluginManager().registerListener(this, new AuthMeListener());
    }

    public static AuthMeBungee getInstance() {
        return instance;
    }
}
