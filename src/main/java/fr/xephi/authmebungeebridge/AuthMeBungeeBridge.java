package fr.xephi.authmebungeebridge;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Xephi59
 *
 */
public class AuthMeBungeeBridge extends Plugin {

    private static AuthMeBungeeBridge instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getProxy().getPluginManager().registerListener(this, new AuthMeListener());
    }

    public static AuthMeBungeeBridge getInstance() {
        return instance;
    }
}
