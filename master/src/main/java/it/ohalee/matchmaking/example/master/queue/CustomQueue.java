package it.ohalee.matchmaking.example.master.queue;

import it.ohalee.ultimatematchmaking.api.MapSupplier;
import it.ohalee.matchmaking.example.match.SharedBedwarsMatch;
import it.ohalee.matchmaking.example.match.enumeration.BedwarsType;

public class CustomQueue extends BedwarsQueue {

    public CustomQueue(String mode, BedwarsType queueType, MapSupplier supplier) {
        super(mode, queueType, supplier);
    }

    public SharedBedwarsMatch customMatch(String who, String map, int maxInTeam, int maxPlayers) {
        SharedBedwarsMatch match = super.initMatch(map);
        match.setMap(map);
        match.setTeamSize(maxInTeam);
        match.setHost(who);
        match.setRequired(maxPlayers);
        return match;
    }

}
