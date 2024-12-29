package it.ohalee.matchmaking.example.master.util;

import it.ohalee.matchmaking.example.master.Main;
import it.ohalee.matchmaking.example.master.queue.BedwarsQueue;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;
import it.ohalee.matchmaking.example.match.enumeration.MatchStatus;

public class MatchMethods {

    public static SharedBedwarsMatch[] availableMatches(BedwarsType type) {
        BedwarsQueue queue = Main.getUM_PROXY().getQueue(type);

        return queue.getMatches()
                .parallelStream()
                .filter(match -> match.getInstanceStatus() == MatchStatus.WAITING || match.getInstanceStatus() == MatchStatus.STARTING)
                .toArray(SharedBedwarsMatch[]::new);
    }

}
