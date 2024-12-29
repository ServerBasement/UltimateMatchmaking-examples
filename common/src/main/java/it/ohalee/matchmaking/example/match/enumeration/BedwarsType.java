package it.ohalee.matchmaking.example.match.enumeration;

import it.ohalee.ultimatematchmaking.match.UMType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BedwarsType implements UMType {
    SOLO("Solo", 1, 8),
    DOUBLES("Duo", 2, 8),
    TRIO("Trio", 3, 4),
    SQUAD("Squad", 4, 4),

    PRIVATE("Private", -1, -1);

    private final static BedwarsType[] defaults = new BedwarsType[]{SOLO, DOUBLES, TRIO, SQUAD};

    private final String visualName;
    private final int teamSize;
    private final int teams;

    public static BedwarsType[] defaults() {
        return defaults;
    }

    @Override
    public String visualName() {
        return visualName;
    }

    @Override
    public int teams() {
        return this.teams;
    }

    @Override
    public int teamSize() {
        return teamSize;
    }

    @Override
    public String toString() {
        return this.name();
    }

}