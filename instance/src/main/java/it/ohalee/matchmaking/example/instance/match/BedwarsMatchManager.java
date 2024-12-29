package it.ohalee.matchmaking.example.instance.match;

import it.ohalee.matchmaking.example.instance.arena.Arena;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.match.IncomingSharedListener;
import it.ohalee.ultimatematchmaking.match.UMMatchManager;

public class BedwarsMatchManager extends UMMatchManager<BedwarsType, SharedBedwarsMatch, Arena> {

    public BedwarsMatchManager(IncomingSharedListener<BedwarsType, SharedBedwarsMatch, Arena> listener) {
        super(listener);
    }

    @Override
    public Class<BedwarsType> typeClass() {
        return BedwarsType.class;
    }

    @Override
    public String getLobby() {
        return "bedwars_lobby_";
    }

    @Override
    public void removeMatch(Arena match) {
        System.out.println("Removing match in progress... " + match.getWorldName());
        super.removeMatch(match);
        System.out.println("Match removed: " + match.getWorldName());
    }
}