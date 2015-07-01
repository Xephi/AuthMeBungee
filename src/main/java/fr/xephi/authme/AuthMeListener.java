package fr.xephi.authme;

import fr.xephi.authme.database.DataSource;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthMeListener implements Listener {

    private DataSource database;

    public AuthMeListener(DataSource database) {
        this.database = database;
    }

    @EventHandler
    public void onCommand(
            net.md_5.bungee.api.event.PermissionCheckEvent event) {
        if (event.getPermission().equalsIgnoreCase("bungeecord.command.server")) {
            CommandSender sender = event.getSender();
            if (!isLogged(sender.getName().toLowerCase()))
                event.setHasPermission(false);
        }
    }

    private boolean isLogged(String name) {
        return (this.database.isLogged(name));
    }
}
