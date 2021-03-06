package fr.xephi.authmebungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class AuthMeListener implements Listener {

    private HashSet<String> logged = new HashSet<String>();

    public AuthMeListener() {
    }

    @EventHandler
    public void onCommand(
            net.md_5.bungee.api.event.PermissionCheckEvent event) {
        if (!Settings.revokePermissions.contains(event.getPermission().toLowerCase())) {
            CommandSender sender = event.getSender();
            if (!(sender instanceof ProxiedPlayer))
            	return;
            if (!isLogged(sender.getName().toLowerCase()))
                event.setHasPermission(false);
        }
    }

    @EventHandler
    public void onMessage(net.md_5.bungee.api.event.PluginMessageEvent event)
    {
    	if (event.isCancelled())
    		return;
    	if (!event.getTag().equalsIgnoreCase("BungeeCord"))
    		return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		try {
			String task = in.readUTF();
	        if (!task.equals("AuthMe")) {
	            return;
	        }
	        String message = in.readUTF();
	        if (message.startsWith("login;"))
	        	logged.add(message.substring("login;".length()));
	        else if (message.startsWith("logout;"))
	        	logged.remove(message.substring("logout;".length()));

		} catch (IOException e) {

		}
    }

    @EventHandler
    public void onLeave(
    		net.md_5.bungee.api.event.PlayerDisconnectEvent event)
    {
    	if (Settings.isAuthMeSessionEnabled)
    		return;
    	logged.remove(event.getPlayer().getName().toLowerCase());
    }

    private boolean isLogged(String name) {
        return (logged.contains(name));
    }
}
