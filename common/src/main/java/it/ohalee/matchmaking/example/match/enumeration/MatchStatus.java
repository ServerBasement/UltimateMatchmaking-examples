package it.ohalee.matchmaking.example.match.enumeration;

public enum MatchStatus {
    WAITING, // Waiting for players to join
    STARTING, // Waiting for players to join, but in countdown (can be full)
    PLAYING, // Match is playing
    RESTARTING // Match is restarting
}