package it.ohalee.matchmaking.example.instance.listener;

import it.ohalee.matchmaking.example.instance.arena.Arena;
import it.ohalee.matchmaking.example.instance.arena.StandardArenaManager;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.player.PlayerReceiver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;

public class ConnectionListener extends PlayerReceiver<BedwarsType, SharedBedwarsMatch, Arena> implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        Arena arena = StandardArenaManager.arena(e.getPlayer().getWorld());

        switch (arena.getStatus()) {
            case WAITING:
            case STARTING:
                // do stuff?
                break;
            case PLAYING:
                if (!e.getPlayer().hasPermission(this.bypassPermission())) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cYou can't join in this match");
                    joining.remove(e.getPlayer().getName());
                }
                break;
        }
    }

    @Override
    public String bypassPermission() {
        return "bedwars.bypass"; // basically spectator permission
    }

    @Override
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);

        joining.remove(player.getName());
        StandardArenaManager.arena(player.getWorld()).letJoin(player);

        e.setJoinMessage(null);
    }

    @Override
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        processQuit(e.getPlayer());
        e.setQuitMessage(null);
    }

    @Override
    @EventHandler
    public void onLeave(PlayerKickEvent e) {
        processQuit(e.getPlayer());
        e.setLeaveMessage(null);
    }

    private void processQuit(Player player) {
        StandardArenaManager.arena(player.getWorld()).letQuit(player);
    }

}