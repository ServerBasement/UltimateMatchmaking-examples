package it.ohalee.matchmaking.example.master;

import it.ohalee.matchmaking.example.master.player.BedwarsParticipator;
import it.ohalee.matchmaking.example.master.queue.BedwarsQueue;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.UltimateMatchmaking;
import org.bukkit.plugin.java.JavaPlugin;

public class UMBedwars extends UltimateMatchmaking<BedwarsType, SharedBedwarsMatch, BedwarsParticipator, BedwarsQueue> {

    public UMBedwars(JavaPlugin plugin) {
        super(plugin);
    }

}