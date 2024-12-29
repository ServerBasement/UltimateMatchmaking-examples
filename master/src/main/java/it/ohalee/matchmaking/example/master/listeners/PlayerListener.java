package it.ohalee.matchmaking.example.master.listeners;

import it.ohalee.matchmaking.example.master.player.BedwarsParticipator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        BedwarsParticipator.prepare(e.getPlayer());
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e) {
        BedwarsParticipator.unload(e.getPlayer());
    }

    @EventHandler
    public void onLeft(PlayerKickEvent e) {
        BedwarsParticipator.unload(e.getPlayer());
    }

}
