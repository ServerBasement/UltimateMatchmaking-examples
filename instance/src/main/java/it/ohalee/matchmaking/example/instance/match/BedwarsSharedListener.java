package it.ohalee.matchmaking.example.instance.match;

import it.ohalee.matchmaking.example.instance.Main;
import it.ohalee.matchmaking.example.instance.arena.Arena;
import it.ohalee.matchmaking.example.instance.arena.StandardArenaManager;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.ultimatematchmaking.match.IncomingSharedListener;

import java.util.concurrent.CompletableFuture;

public class BedwarsSharedListener extends IncomingSharedListener<BedwarsType, SharedBedwarsMatch, Arena> {

    @Override
    public Class<SharedBedwarsMatch> getMatchClass() {
        return SharedBedwarsMatch.class;
    }

    @Override
    public CompletableFuture<Arena> instantiate(SharedBedwarsMatch sharedBedwarsMatch, String key, String value) {
        return StandardArenaManager.loadArena(Main.getInstance(), sharedBedwarsMatch, value);
    }

}