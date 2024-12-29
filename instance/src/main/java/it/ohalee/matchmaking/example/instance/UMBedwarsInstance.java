package it.ohalee.matchmaking.example.instance;

import it.ohalee.ultimatematchmaking.UMSub;
import it.ohalee.matchmaking.example.instance.arena.Arena;
import it.ohalee.matchmaking.example.instance.listener.ConnectionListener;
import it.ohalee.matchmaking.example.instance.match.BedwarsMatchManager;
import it.ohalee.matchmaking.example.instance.match.BedwarsSharedListener;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.match.UMMatchManager;
import it.ohalee.ultimatematchmaking.player.PlayerReceiver;

public class UMBedwarsInstance extends UMSub<BedwarsType, SharedBedwarsMatch, Arena> {

    public UMBedwarsInstance(Main plugin) {
        super(plugin, null);
    }

    @Override
    public UMMatchManager<BedwarsType, SharedBedwarsMatch, Arena> summonMatchManager() {
        return new BedwarsMatchManager(new BedwarsSharedListener());
    }

    @Override
    public PlayerReceiver<BedwarsType, SharedBedwarsMatch, Arena> summonPlayerReceiver() {
        return new ConnectionListener();
    }

}